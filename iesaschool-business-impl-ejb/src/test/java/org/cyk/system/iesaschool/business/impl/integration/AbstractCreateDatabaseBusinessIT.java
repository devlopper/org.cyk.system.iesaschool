package org.cyk.system.iesaschool.business.impl.integration;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.root.business.api.IdentifiableBusinessService.CompleteInstanciationOfOneFromValuesListener;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness.CompleteActorInstanciationOfManyFromValuesArguments;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.root.model.party.person.Sex;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;
import org.cyk.utility.common.Constant;

public abstract class AbstractCreateDatabaseBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private ClassroomSession pkg,kg1,kg2,kg3,g1A,g1B,g2,g3A,g3B,g4A,g4B,g5A,g5B,g6,g7,g8,g9,g10,g11,g12;
    
    @Override
    protected void businesses() {
    	installApplication();
    	for(ClassroomSession classroomSession : classroomSessionDao.readAll())
    		if(classroomSession.getLevelTimeDivision().getIndex() == 0)
    			pkg = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 1)
    			kg1 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 2)
    			kg2 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 3)
    			kg3 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 4){
    			if(classroomSession.getSuffix().equals("A"))
    				g1A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g1B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getIndex() == 5)
    			g2 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 6){
    			if(classroomSession.getSuffix().equals("A"))
    				g3A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g3B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getIndex() == 7){
    			if(classroomSession.getSuffix().equals("A"))
    				g4A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g4B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getIndex() == 8){
    			if(classroomSession.getSuffix().equals("A"))
    				g5A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g5B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getIndex() == 9)
    			g6 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 10)
    			g7 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 11)
    			g8 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 12)
    			g9 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 13)
    			g10 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 14)
    			g11 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getIndex() == 15)
    			g12 = classroomSession;
    	
    	File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data");
		File file = new File(directory, "data.xlsx");
		try {
			processTeachersSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"));
			
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),1,2,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),1,40,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),1,78,3);

			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),2,2,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),2,24,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),2,43,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),2,69,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),2,85,3);
			
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),3,2,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),3,23,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),3,38,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),3,52,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),3,63,3);
			
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),4,2,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),4,20,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),4,44,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),4,69,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),4,84,3);
			processStudentsSheet(file,new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature"),4,95,3);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.exit(0);
    }
    
    private void processTeachersSheet(File file,final File signatureDirectory) throws Exception{
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(0);
    	readExcelSheetArguments.setFromRowIndex(2);
    	readExcelSheetArguments.setFromColumnIndex(1);
		
		CompleteActorInstanciationOfManyFromValuesArguments<Teacher> completeActorInstanciationOfManyFromValuesArguments = new CompleteActorInstanciationOfManyFromValuesArguments<>();
		
		completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setRegistrationCodeIndex(0);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setTitleCodeIndex(1);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setNameIndex(2);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setLastnameIndex(3);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setListener(new CompleteInstanciationOfOneFromValuesListener<Teacher>() {
			@Override
			public void beforeProcessing(Teacher teacher,String[] values) {
				values[1] = getPersonTitleCode(values[1]);
			}
    		@Override
			public void afterProcessing(Teacher teacher,String[] values) {
    			//teacher.getPerson().getExtendedInformations().getTitle().setCode(getPersonTitleCode(teacher.getPerson().getExtendedInformations().getTitle().getCode()));
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
    
    	System.out.println("Instanciating teachers");
    	List<Teacher> teachers = schoolBusinessLayer.getTeacherBusiness().instanciateMany(readExcelSheetArguments, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.println("Creating teachers");
    	schoolBusinessLayer.getTeacherBusiness().create(teachers);
    }
    
    private void processStudentsSheet(File file,final File imageDirectory,Integer sheetIndex,Integer fromRowIndex,Integer count) throws Exception{
    	final Collection<StudentClassroomSession> studentClassroomSessions = new ArrayList<>();
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(sheetIndex);
    	readExcelSheetArguments.setFromRowIndex(fromRowIndex);
    	readExcelSheetArguments.setFromColumnIndex(1);
    	readExcelSheetArguments.setRowCount(count);
    	
		CompleteActorInstanciationOfManyFromValuesArguments<Student> completeActorInstanciationOfManyFromValuesArguments = new CompleteActorInstanciationOfManyFromValuesArguments<>();
		
		completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setRegistrationCodeIndex(0);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setNameIndex(1);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setLastnameIndex(2);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setBirthDateIndex(3);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setBirthLocationOtherDetailsIndex(4);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setSexCodeIndex(5);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setListener(new CompleteInstanciationOfOneFromValuesListener<Student>() {
			@Override
			public void beforeProcessing(Student student,String[] values) {
				studentClassroomSessions.add(new StudentClassroomSession(student, getClassroomSession(values[6])));
				
			}
    		@Override
			public void afterProcessing(Student student,String[] values) {
    			student.getPerson().getSex().setCode(getSexCode(student.getPerson().getSex().getCode()));
    			File signatureFile = new File(imageDirectory,StringUtils.replace(student.getRegistration().getCode(),"/","")+".png");
				if(signatureFile.exists())
					try {
						student.getPerson().setImage(RootBusinessLayer.getInstance().getFileBusiness()
							.process(IOUtils.toByteArray(new FileInputStream(signatureFile)), "photo.png"));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
    	System.out.println("Instanciating students");
    	List<Student> students = schoolBusinessLayer.getStudentBusiness().instanciateMany(readExcelSheetArguments, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.println("Creating "+students.size()+" students");
    	schoolBusinessLayer.getStudentBusiness().create(students);
    	System.out.println("Creating "+studentClassroomSessions.size()+" student classroom sessions");
    	schoolBusinessLayer.getStudentClassroomSessionBusiness().create(studentClassroomSessions);
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
    
    private String getSexCode(String code){
    	if(code.equals("M"))
			return Sex.MALE;
		else if(code.equals("F"))
			return Sex.FEMALE;
    	return null;
    }
    
    private ClassroomSession getClassroomSession(String code){
    	code = StringUtils.replace(code, Constant.CHARACTER_SPACE.toString(), Constant.EMPTY_STRING).toLowerCase();
    	if(code.equals("pre-k") || code.equals("pk"))
			return pkg;
    	if(code.equals("kg1"))
			return kg1;
    	if(code.equals("kg2"))
			return kg2;
    	if(code.equals("kg3"))
			return kg3;
    	if(code.equals("g1a"))
			return g1A;
    	if(code.equals("g1b"))
			return g1B;
    	if(code.equals("g2"))
			return g2;
    	if(code.equals("g3a"))
			return g3A;
    	if(code.equals("g3b"))
			return g3B;
    	if(code.equals("g4a"))
			return g4A;
    	if(code.equals("g4b"))
			return g4B;
    	if(code.equals("g5a"))
			return g5A;
    	if(code.equals("g5b"))
			return g5B;
    	if(code.equals("g6"))
			return g6;
    	if(code.equals("g7"))
			return g7;
    	if(code.equals("g8"))
			return pkg;
    	if(code.equals("g9"))
			return g9;
    	if(code.equals("g10"))
			return g10;
    	if(code.equals("g11"))
			return g11;
    	if(code.equals("g12"))
			return g12;
    	return null;
    }

}
