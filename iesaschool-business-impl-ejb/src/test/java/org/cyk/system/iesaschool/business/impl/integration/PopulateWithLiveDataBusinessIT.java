package org.cyk.system.iesaschool.business.impl.integration;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.party.person.PersonExtendedInformations;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;


public class PopulateWithLiveDataBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	installApplication();

    	Collection<Teacher> teachers = new ArrayList<>();
    	Collection<Student> students = new ArrayList<>();
    	
    	File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data");
		ReadExcelSheetArguments arguments = new ReadExcelSheetArguments();
		try {
			arguments.setWorkbookInputStream(new FileInputStream(new File(directory, "data.xlsx")));
			arguments.setSheetIndex(0);
			arguments.setFromRowIndex(2);
			arguments.setFromColumnIndex(1);
			List<String[]> list = CommonUtils.getInstance().readExcelSheet(arguments);
			System.out.println("###   Teachers   ###");
			for(String[] line : list){
				Teacher teacher = new Teacher();
				teacher.getRegistration().setCode(line[0]);
				teacher.setPerson(new Person());
				teacher.getPerson().setName(line[2]);
				teacher.getPerson().setLastName(line[3]);
				teacher.getPerson().setExtendedInformations(new PersonExtendedInformations(teacher.getPerson()));
				teacher.getPerson().getExtendedInformations().setTitle(new PersonTitle());
				if(line[1].equals("Mrs."))
					teacher.getPerson().getExtendedInformations().getTitle().setCode(PersonTitle.MISS);
				else if(line[1].equals("Ms."))
					teacher.getPerson().getExtendedInformations().getTitle().setCode(PersonTitle.MADAM);
				else if(line[1].equals("Mr."))
					teacher.getPerson().getExtendedInformations().getTitle().setCode(PersonTitle.MISTER);
				teachers.add(teacher);
			}
			SchoolBusinessLayer.getInstance().getTeacherBusiness().create(teachers);
			/*
			arguments.setWorkbookInputStream(new FileInputStream(new File(directory, "data.xlsx")));
			arguments.setSheetIndex(1);
			arguments.setFromRowIndex(2);
			arguments.setFromColumnIndex(1);
			list = CommonUtils.getInstance().readExcelSheet(arguments);
			System.out.println("###   Students(PK to K3)   ###");
			for(String[] line : list)
				System.out.println(StringUtils.join(line," ; "));
				*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
