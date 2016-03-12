package org.cyk.system.iesaschool.business.impl.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.root.business.api.IdentifiableBusinessService.CompleteInstanciationOfOneFromValuesListener;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness.CompleteActorInstanciationOfOneFromValuesArguments;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;

public abstract class AbstractCreateDatabaseBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	installApplication();
    	File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data");
		File file = new File(directory, "data.xlsx");
		try {
			processTeachersSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.exit(0);
    }
    
    private void processTeachersSheet(File file,final File signatureDirectory) throws Exception{
    	ReadExcelSheetArguments arguments = new ReadExcelSheetArguments();
    	arguments.setWorkbookInputStream(new FileInputStream(file));
		arguments.setSheetIndex(0);
		arguments.setFromRowIndex(2);
		arguments.setFromColumnIndex(1);
		List<String[]> list = CommonUtils.getInstance().readExcelSheet(arguments);
    	Collection<Teacher> teachers = new ArrayList<>();
    	CompleteActorInstanciationOfOneFromValuesArguments<Teacher> completeActorInstanciationOfOneFromValuesArguments = new CompleteActorInstanciationOfOneFromValuesArguments<>();
    	completeActorInstanciationOfOneFromValuesArguments.setRegistrationCodeIndex(0);
    	completeActorInstanciationOfOneFromValuesArguments.getPersonInstanciationOfOneFromValuesArguments().setTitleCodeIndex(1);
    	completeActorInstanciationOfOneFromValuesArguments.getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setNameIndex(2);
    	completeActorInstanciationOfOneFromValuesArguments.getPersonInstanciationOfOneFromValuesArguments().setLastnameIndex(3);
    	completeActorInstanciationOfOneFromValuesArguments.setListener(new CompleteInstanciationOfOneFromValuesListener<Teacher>() {
			@Override
			public void processed(Teacher teacher) {
				File signatureFile = new File(signatureDirectory,StringUtils.replace(teacher.getRegistration().getCode(),"/","")+".png");
				if(signatureFile.exists())
					try {
						teacher.getPerson().getExtendedInformations().setSignatureSpecimen(RootBusinessLayer.getInstance().getFileBusiness()
							.process(IOUtils.toByteArray(new FileInputStream(signatureFile)), "signature.png"));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
    	
    	for(String[] values : list){
    		completeActorInstanciationOfOneFromValuesArguments.setValues(values);
    		Teacher teacher = new Teacher();
    		values[1] = getPersonTitleCode(values[1]);
			SchoolBusinessLayer.getInstance().getTeacherBusiness().completeInstanciationOfOneFromValues(teacher, completeActorInstanciationOfOneFromValuesArguments);
			/*
			File signatureFile = new File(signatureDirectory,StringUtils.replace(teacher.getRegistration().getCode(),"/","")+".png");
			if(signatureFile.exists())
				teacher.getPerson().getExtendedInformations().setSignatureSpecimen(RootBusinessLayer.getInstance().getFileBusiness()
					.process(IOUtils.toByteArray(new FileInputStream(signatureFile)), "signature.png"));
			*/
			teachers.add(teacher);
		}
		SchoolBusinessLayer.getInstance().getTeacherBusiness().completeInstanciationOfMany(teachers);
		SchoolBusinessLayer.getInstance().getTeacherBusiness().create(teachers);
    }
    
    private String getPersonTitleCode(String code){
    	if(code.equals("Mrs."))
			return PersonTitle.MISS;
		else if(code.equals("Ms."))
			return PersonTitle.MADAM;
		else if(code.equals("Mr."))
			return PersonTitle.MISTER;
    	return null;
    }

}
