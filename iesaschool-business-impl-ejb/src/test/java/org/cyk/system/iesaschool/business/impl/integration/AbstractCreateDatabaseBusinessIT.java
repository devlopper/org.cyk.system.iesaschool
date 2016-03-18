package org.cyk.system.iesaschool.business.impl.integration;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.IdentifiableBusinessService.CompleteInstanciationOfOneFromValuesListener;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness.CompleteActorInstanciationOfManyFromValuesArguments;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.root.model.party.person.Sex;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.SchoolDataProducerHelper;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;
import org.cyk.utility.common.Constant;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public abstract class AbstractCreateDatabaseBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private ClassroomSession pkg,kg1,kg2,kg3,g1A,g1B,g2,g3A,g3B,g4A,g4B,g5A,g5B,g6,g7,g8,g9,g10,g11,g12;
    
    @Override
    protected void businesses() {
    	SchoolDataProducerHelper.Listener.COLLECTION.add(new SchoolDataProducerHelper.Listener.Adapter.Default(){
			private static final long serialVersionUID = -5301917191935456060L;

			@Override
    		public void classroomSessionDivisionCreated(ClassroomSessionDivision classroomSessionDivision) {
    			super.classroomSessionDivisionCreated(classroomSessionDivision);
    			classroomSessionDivision.getPeriod().setFromDate(new DateTime(2016, 4, 4, 0, 0).toDate());
    			classroomSessionDivision.getPeriod().setToDate(new DateTime(2016, 6, 13, 0, 0).toDate());
    			classroomSessionDivision.setDuration(48l * DateTimeConstants.MILLIS_PER_DAY);
    		}
    	});
    	installApplication();
    	File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data");
		File excelWorkbookFile = new File(directory, "data.xlsx")
			,teachersSignatureDirectory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature")
			,studentsPhotoDirectory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\photo");
		
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
    	
    	try {
			processTeachersSheet(excelWorkbookFile,teachersSignatureDirectory);
			processCoordinatorsSheet(excelWorkbookFile);
			processClassroomSessionDivisionSubjectTeachersSheet(excelWorkbookFile);
			
			processStudentsSheet(pkg,excelWorkbookFile,studentsPhotoDirectory,1,2,34);
			processStudentsSheet(kg1,excelWorkbookFile,studentsPhotoDirectory,1,38,38);
			processStudentsSheet(kg2,excelWorkbookFile,studentsPhotoDirectory,1,78,38);
			processStudentsSheet(kg3,excelWorkbookFile,studentsPhotoDirectory,1,118,26);

			processStudentsSheet(g1A,excelWorkbookFile,studentsPhotoDirectory,2,2,20);
			processStudentsSheet(g1B,excelWorkbookFile,studentsPhotoDirectory,2,24,18);
			processStudentsSheet(g2,excelWorkbookFile,studentsPhotoDirectory,2,43,25);
			processStudentsSheet(g3A,excelWorkbookFile,studentsPhotoDirectory,2,69,14);
			processStudentsSheet(g3B,excelWorkbookFile,studentsPhotoDirectory,2,85,14);
			
			processStudentsSheet(g4A,excelWorkbookFile,studentsPhotoDirectory,3,2,19);
			processStudentsSheet(g4B,excelWorkbookFile,studentsPhotoDirectory,3,23,13);
			processStudentsSheet(g5A,excelWorkbookFile,studentsPhotoDirectory,3,38,12);
			processStudentsSheet(g5B,excelWorkbookFile,studentsPhotoDirectory,3,52,9);
			processStudentsSheet(g6,excelWorkbookFile,studentsPhotoDirectory,3,63,18);
			
			processStudentsSheet(g7,excelWorkbookFile,studentsPhotoDirectory,4,2,16);
			processStudentsSheet(g8,excelWorkbookFile,studentsPhotoDirectory,4,20,22);
			processStudentsSheet(g9,excelWorkbookFile,studentsPhotoDirectory,4,44,23);
			processStudentsSheet(g10,excelWorkbookFile,studentsPhotoDirectory,4,69,13);
			processStudentsSheet(g11,excelWorkbookFile,studentsPhotoDirectory,4,84,9);
			processStudentsSheet(g12,excelWorkbookFile,studentsPhotoDirectory,4,95,1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	/*if(Boolean.TRUE.equals(isSimulated())){
    		schoolBusinessTestHelper.createSubjectEvaluations(Boolean.FALSE);
    		schoolBusinessTestHelper.randomValues(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
    	}*/
    	
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
    
    	System.out.print("Instanciating teachers");
    	List<Teacher> teachers = schoolBusinessLayer.getTeacherBusiness().instanciateMany(readExcelSheetArguments, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.println(" - Creating "+teachers.size()+" teachers");
    	schoolBusinessLayer.getTeacherBusiness().create(teachers);
    }
    
    private void processCoordinatorsSheet(File file) throws Exception{
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(5);
    	readExcelSheetArguments.setFromRowIndex(2);
		List<String[]> list = CommonUtils.getInstance().readExcelSheet(readExcelSheetArguments);
    	Collection<ClassroomSession> classroomSessions = new ArrayList<>();
		for(String[] values : list){
    		ClassroomSession classroomSession = getClassroomSession(values[0]);
    		classroomSession.setCoordinator(SchoolBusinessLayer.getInstance().getTeacherBusiness().findByRegistrationCode(values[2]));
    		classroomSessions.add(classroomSession);
    	}
		System.out.println("Updating "+classroomSessions.size()+" class room sessions");
		SchoolBusinessLayer.getInstance().getClassroomSessionBusiness().update(classroomSessions);
    }
    
    private void processClassroomSessionDivisionSubjectTeachersSheet(File file) throws Exception{
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(8);
    	readExcelSheetArguments.setFromRowIndex(1);
		List<String[]> list = CommonUtils.getInstance().readExcelSheet(readExcelSheetArguments);
    	Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects = new ArrayList<>();
		for(String[] values : list){
			//System.out.println("V : "+getClassroomSession(values[0])+" , "+getSubject(values[1])+" : "+classroomSessionDivisionSubjectDao
    		//		.readByClassroomSessionBySubject(getClassroomSession(values[0]),getSubject(values[1])));
			for(ClassroomSessionDivisionSubject classroomSessionDivisionSubject : classroomSessionDivisionSubjectDao
    				.readByClassroomSessionBySubject(getClassroomSession(values[0]),getSubject(values[1]))){
    			
    			classroomSessionDivisionSubject.setTeacher(SchoolBusinessLayer.getInstance().getTeacherBusiness().findByRegistrationCode(values[2]));
    			classroomSessionDivisionSubjects.add(classroomSessionDivisionSubject);
    		}
    	}
		System.out.println("Updating "+classroomSessionDivisionSubjects.size()+" class room session division subjects");
		SchoolBusinessLayer.getInstance().getClassroomSessionDivisionSubjectBusiness().update(classroomSessionDivisionSubjects);
    }
    
    private void processStudentsSheet(ClassroomSession classroomSession,File file,final File imageDirectory,Integer sheetIndex,Integer fromRowIndex,Integer count) throws Exception{
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
    			if(student.getPerson().getSex()!=null)
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
    	System.out.print(classroomSession);
    	System.out.print(" - Instanciating students");
    	List<Student> students = schoolBusinessLayer.getStudentBusiness().instanciateMany(readExcelSheetArguments, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.print(" - Creating "+students.size()+" students");
    	schoolBusinessLayer.getStudentBusiness().create(students);
    	System.out.println(" - Creating "+studentClassroomSessions.size()+" student classroom sessions");
    	schoolBusinessLayer.getStudentClassroomSessionBusiness().create(studentClassroomSessions);
    	genericBusiness.flushEntityManager();
    	//System.out.println("Number of students : "+ classroomSessionDao.read(classroomSession.getIdentifier()).getNumberOfStudents());
    	
    	if(Boolean.TRUE.equals(isSimulated())){
    		schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionSubjectDao.readByClassroomSession(classroomSession),Boolean.FALSE);
    		schoolBusinessTestHelper.randomValues(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    		genericBusiness.flushEntityManager();
    	}
    }
    
    private String getPersonTitleCode(String code){
    	if(code.equals("Mrs."))
			return PersonTitle.MISS;
		else if(code.equals("Ms."))
			return PersonTitle.MADAM;
		else if(code.equals("Mr."))
			return PersonTitle.MISTER;
    	return code;
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
			return g8;
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
    
    private Subject getSubject(String code){
    	if(code.equals("S17"))
    		code = IesaConstant.SUBJECT_GRAMMAR_CODE;
    	else if(code.equals("S21"))
    		code = IesaConstant.SUBJECT_FRENCH_CODE;
    	
    	return SchoolBusinessLayer.getInstance().getSubjectBusiness().find(code);
    }

    protected Boolean isSimulated(){
    	return Boolean.FALSE;
    }
}
