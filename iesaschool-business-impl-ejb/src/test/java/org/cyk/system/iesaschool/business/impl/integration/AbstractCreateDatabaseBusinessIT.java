package org.cyk.system.iesaschool.business.impl.integration;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.IdentifiableBusinessService.CompleteInstanciationOfOneFromValuesListener;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.mathematics.IntervalBusiness;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness.CompleteActorInstanciationOfManyFromValuesArguments;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.root.model.party.person.Sex;
import org.cyk.system.school.business.api.actor.StudentBusiness;
import org.cyk.system.school.business.api.actor.TeacherBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.StudentClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.StudentClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectEvaluationTypeBusiness;
import org.cyk.system.school.business.api.subject.EvaluationBusiness;
import org.cyk.system.school.business.api.subject.EvaluationTypeBusiness;
import org.cyk.system.school.business.api.subject.StudentClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.business.api.subject.SubjectBusiness;
import org.cyk.system.school.business.impl.SchoolDataProducerHelper;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubjectEvaluationType;
import org.cyk.system.school.model.subject.Evaluation;
import org.cyk.system.school.model.subject.StudentClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.StudentClassroomSessionDivisionSubjectEvaluation;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;
import org.cyk.utility.common.Constant;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public abstract class AbstractCreateDatabaseBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private ClassroomSession pkg,kg1,kg2,kg3,g1A,g1B,g2,g3A,g3B,g4A,g4B,g5A,g5B,g6,g7,g8,g9,g10,g11,g12;
    private Collection<Subject> subjects;
    
    @Override
    protected void businesses() {
    	subjects = inject(SubjectBusiness.class).findAll();
    	SchoolDataProducerHelper.Listener.COLLECTION.add(new SchoolDataProducerHelper.Listener.Adapter.Default(){
			private static final long serialVersionUID = -5301917191935456060L;

			@Override
    		public void classroomSessionDivisionCreated(ClassroomSessionDivision classroomSessionDivision) {
    			super.classroomSessionDivisionCreated(classroomSessionDivision);
    			classroomSessionDivision.setBirthDate(new DateTime(2016, 4, 4, 0, 0).toDate());
    			classroomSessionDivision.setDeathDate(new DateTime(2016, 6, 13, 0, 0).toDate());
    			classroomSessionDivision.getExistencePeriod().getNumberOfMillisecond().setUser(new BigDecimal(48l * DateTimeConstants.MILLIS_PER_DAY));
    		}
			
			@Override
			public void classroomSessionDivisionSubjectEvaluationTypeCreated(ClassroomSessionDivisionSubjectEvaluationType classroomSessionDivisionSubjectEvaluationType) {
				super.classroomSessionDivisionSubjectEvaluationTypeCreated(classroomSessionDivisionSubjectEvaluationType);
				classroomSessionDivisionSubjectEvaluationType.setCountInterval(inject(IntervalBusiness.class).instanciateOne(null
						, RandomStringUtils.randomAlphanumeric(6), "1", "1"));
			}
    	});
    	installApplication();
    	if(Boolean.TRUE.equals(noData()))
    		return;
    	File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data");
		File excelWorkbookFile = new File(directory, "data.xlsx")
			,teachersSignatureDirectory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\signature")
			,studentsPhotoDirectory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\photo")
			,previousDatabasesFile = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\previousdatabase.xlsx");
		
    	for(ClassroomSession classroomSession : classroomSessionDao.readAll())
    		if(classroomSession.getLevelTimeDivision().getOrderNumber() == 0)
    			pkg = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 1)
    			kg1 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 2)
    			kg2 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 3)
    			kg3 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 4){
    			if(classroomSession.getSuffix().equals("A"))
    				g1A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g1B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 5)
    			g2 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 6){
    			if(classroomSession.getSuffix().equals("A"))
    				g3A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g3B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 7){
    			if(classroomSession.getSuffix().equals("A"))
    				g4A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g4B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 8){
    			if(classroomSession.getSuffix().equals("A"))
    				g5A = classroomSession;
    			else if(classroomSession.getSuffix().equals("B"))
    				g5B = classroomSession;
    		}else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 9)
    			g6 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 10)
    			g7 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 11)
    			g8 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 12)
    			g9 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 13)
    			g10 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 14)
    			g11 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getOrderNumber() == 15)
    			g12 = classroomSession;
    	
    	try {
			processTeachersSheet(excelWorkbookFile,teachersSignatureDirectory);
			processCoordinatorsSheet(excelWorkbookFile);
			processClassroomSessionDivisionSubjectTeachersSheet(excelWorkbookFile);
			/*
			processStudentsSheet(pkg,excelWorkbookFile,studentsPhotoDirectory,1,2,34);
			processStudentsSheet(kg1,excelWorkbookFile,studentsPhotoDirectory,1,38,38);
			processStudentsSheet(kg2,excelWorkbookFile,studentsPhotoDirectory,1,78,38);
			processStudentsSheet(kg3,excelWorkbookFile,studentsPhotoDirectory,1,118,26);
			*/

			processStudentsSheet(g1A,excelWorkbookFile,studentsPhotoDirectory,2,2,20);
			processStudentsSheet(g1B,excelWorkbookFile,studentsPhotoDirectory,2,24,18);
			processStudentsSheet(g2,excelWorkbookFile,studentsPhotoDirectory,2,43,25);
			processStudentsSheet(g3A,excelWorkbookFile,studentsPhotoDirectory,2,69,14);
			processStudentsSheet(g3B,excelWorkbookFile,studentsPhotoDirectory,2,85,14);
			
			processStudentsSheet(g4A,excelWorkbookFile,studentsPhotoDirectory,3,2,19);
			processStudentsSheet(g4B,excelWorkbookFile,studentsPhotoDirectory,3,23,13);//0 
			processStudentsSheet(g5A,excelWorkbookFile,studentsPhotoDirectory,3,38,12);
			processStudentsSheet(g5B,excelWorkbookFile,studentsPhotoDirectory,3,52,9);
			processStudentsSheet(g6,excelWorkbookFile,studentsPhotoDirectory,3,63,18);
			
			processStudentsSheet(g7,excelWorkbookFile,studentsPhotoDirectory,4,2,16);
			processStudentsSheet(g8,excelWorkbookFile,studentsPhotoDirectory,4,20,22);
			processStudentsSheet(g9,excelWorkbookFile,studentsPhotoDirectory,4,44,23);
			processStudentsSheet(g10,excelWorkbookFile,studentsPhotoDirectory,4,69,13);
			processStudentsSheet(g11,excelWorkbookFile,studentsPhotoDirectory,4,84,9);
			processStudentsSheet(g12,excelWorkbookFile,studentsPhotoDirectory,4,95,1);
			
			//processPreviousDatabases(previousDatabasesFile);
			processPreviousDatabasesStudentEvaluations(previousDatabasesFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.exit(0);
    }
    
    protected Boolean noData(){
    	return Boolean.FALSE;
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
				File signatureFile = new File(signatureDirectory,StringUtils.replace(teacher.getCode(),"/","")+".png");
				if(signatureFile.exists())
					try {
						teacher.getPerson().getExtendedInformations().setSignatureSpecimen(inject(FileBusiness.class)
							.process(IOUtils.toByteArray(new FileInputStream(signatureFile)), "signature.png"));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
    
    	System.out.print("Instanciating teachers");
    	List<Teacher> teachers = inject(TeacherBusiness.class).instanciateMany(readExcelSheetArguments, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.println(" - Creating "+teachers.size()+" teachers");
    	inject(TeacherBusiness.class).create(teachers);
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
    		classroomSession.setCoordinator(inject(TeacherBusiness.class).find(values[2]));
    		classroomSessions.add(classroomSession);
    	}
		System.out.println("Updating "+classroomSessions.size()+" class room sessions");
		inject(ClassroomSessionBusiness.class).update(classroomSessions);
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
    				.readByClassroomSessionBySubject(getClassroomSession(values[0]),getSubject(values[1],Boolean.TRUE))){
    			
    			classroomSessionDivisionSubject.setTeacher(inject(TeacherBusiness.class).find(values[2]));
    			classroomSessionDivisionSubjects.add(classroomSessionDivisionSubject);
    		}
    	}
		System.out.println("Updating "+classroomSessionDivisionSubjects.size()+" class room session division subjects");
		inject(ClassroomSessionDivisionSubjectBusiness.class).update(classroomSessionDivisionSubjects);
    }
    
    private void processStudentsSheet(ClassroomSession classroomSession,File file,final File imageDirectory,Integer sheetIndex,Integer fromRowIndex,Integer count) throws Exception{
    	final Collection<StudentClassroomSession> studentClassroomSessions = new ArrayList<>();
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(sheetIndex);
    	readExcelSheetArguments.setFromRowIndex(fromRowIndex);
    	readExcelSheetArguments.setFromColumnIndex(0);
    	readExcelSheetArguments.setRowCount(count);
    	
		CompleteActorInstanciationOfManyFromValuesArguments<Student> completeActorInstanciationOfManyFromValuesArguments = new CompleteActorInstanciationOfManyFromValuesArguments<>();
		
		completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setRegistrationCodeIndex(1);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setNameIndex(2);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setLastnameIndex(3);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setBirthDateIndex(4);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setBirthLocationOtherDetailsIndex(5);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setSexCodeIndex(6);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setListener(new CompleteInstanciationOfOneFromValuesListener<Student>() {
			@Override
			public void beforeProcessing(Student student,String[] values) {
				if(!ArrayUtils.contains(new String[]{"g9","g10","g11","g12"}, values[7].toLowerCase()));
					studentClassroomSessions.add(new StudentClassroomSession(student, getClassroomSession(values[7])));
			}
    		@Override
			public void afterProcessing(Student student,String[] values) {
    			if(student.getPerson().getSex()!=null)
    				student.getPerson().getSex().setCode(getSexCode(student.getPerson().getSex().getCode()));
    			File photoFile = new File(imageDirectory,new BigDecimal(values[0]).intValue()+".jpg");
				if(photoFile.exists())
					try {
						student.getPerson().setImage(inject(FileBusiness.class)
							.process(IOUtils.toByteArray(new FileInputStream(photoFile)), "photo.jpeg"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				else
					;//System.out.println("Not found : "+photoFile);
			}
		});
    	System.out.print(classroomSession);
    	System.out.print(" - Instanciating students");
    	List<Student> students = inject(StudentBusiness.class).instanciateMany(readExcelSheetArguments, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.print(" - Creating "+students.size()+" students");
    	inject(StudentBusiness.class).create(students);
    	if(classroomSession.getLevelTimeDivision().getOrderNumber()>3 && classroomSession.getLevelTimeDivision().getOrderNumber()<=11){
    		System.out.println(" - Creating "+studentClassroomSessions.size()+" student classroom sessions");
    		inject(StudentClassroomSessionBusiness.class).create(studentClassroomSessions);
    		genericBusiness.flushEntityManager();
    	}else
    		System.out.println();
    	
    	//System.out.println("Number of students : "+ classroomSessionDao.read(classroomSession.getIdentifier()).getNumberOfStudents());
    	
    	if(Boolean.TRUE.equals(isSimulated())){
    		schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionSubjectDao.readByClassroomSession(classroomSession),Boolean.FALSE);
    		schoolBusinessTestHelper.randomValues(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    		genericBusiness.flushEntityManager();
    	}
    }
    
    public void processPreviousDatabases(File file) throws Exception{
    	List<StudentClassroomSessionDivision> studentClassroomSessionDivisions = new ArrayList<>(inject(StudentClassroomSessionDivisionBusiness.class).findAll());
    	Collection<StudentClassroomSessionDivision> updatedStudentClassroomSessionDivisions = new ArrayList<>();
    	System.out.println(studentClassroomSessionDivisions.size()+" student class room session divisions to update");
    	for(int divisionIndex = 0; divisionIndex < 2 ; divisionIndex++){
    		ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
        	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
        	readExcelSheetArguments.setSheetIndex(divisionIndex);
        	readExcelSheetArguments.setFromRowIndex(1);
    		List<String[]> list = CommonUtils.getInstance().readExcelSheet(readExcelSheetArguments);
    		List<String[]> nf = new ArrayList<>();
    		
    		for(String[] values : list){
    			int i=0;
    			for(;i<studentClassroomSessionDivisions.size();i++){
    				if(studentClassroomSessionDivisions.get(i).getStudent().getCode().equals(values[0].trim()) 
    						&& (studentClassroomSessionDivisions.get(i).getClassroomSessionDivision().getOrderNumber().intValue() == divisionIndex)){
    					break;
    				}
    			}
    			
    			StudentClassroomSessionDivision studentClassroomSessionDivision = i<studentClassroomSessionDivisions.size() 
    					? studentClassroomSessionDivisions.remove(i) : null;
    			if(studentClassroomSessionDivision==null){
    				nf.add(values);
    			}else{
    				studentClassroomSessionDivision.getResults().getEvaluationSort().getAverage().setValue(new BigDecimal(values[1]));
    				updatedStudentClassroomSessionDivisions.add(studentClassroomSessionDivision);
    			}
        		
        	}
    		
    		for(String[] values : nf)
    			if(StringUtils.isNotBlank(values[1]))
    				System.out.println("No student class room session division found for "+StringUtils.join(values,Constant.CHARACTER_COMA));
    	}
    	
    	for(StudentClassroomSessionDivision studentClassroomSessionDivision : studentClassroomSessionDivisions)
    		if(studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber().intValue()<2)
    			System.out.println("No student class room session division has been found for : "+studentClassroomSessionDivision);
    	
    	System.out.println("Updating "+updatedStudentClassroomSessionDivisions.size()+" student class room session divisions");
		inject(StudentClassroomSessionDivisionBusiness.class).update(updatedStudentClassroomSessionDivisions);	
    	
    }
    
    private void processPreviousDatabasesStudentEvaluations(File file) throws Exception{
    	Collection<Student> students = inject(StudentBusiness.class).findAll();
    	Set<Student> studentsExists = new HashSet<>();
    	Collection<StudentClassroomSessionDivisionSubject> studentClassroomSessionDivisionSubjects = inject(StudentClassroomSessionDivisionSubjectBusiness.class).findAll();
    	Set<StudentClassroomSessionDivisionSubject> studentClassroomSessionDivisionSubjectsExists = new HashSet<>();
    	
    	System.out.println("Student classroom session division subjects : "+studentClassroomSessionDivisionSubjects.size());
    	//List<StudentClassroomSessionDivisionSubjectEvaluation> studentClassroomSessionDivisionSubjectEvaluations = new ArrayList<>();
    	Collection<Evaluation> evaluations = new ArrayList<>();
    	for(byte divisionIndex = 0; divisionIndex < 2 ; divisionIndex++){
    		ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
        	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
        	readExcelSheetArguments.setSheetIndex(divisionIndex+2);
        	readExcelSheetArguments.setFromRowIndex(1);
    		List<String[]> list = CommonUtils.getInstance().readExcelSheet(readExcelSheetArguments);
    		
    		Evaluation evaluation = null;
    		String previousEvaluationTypeCode = null;
    		for(String[] values : list){
    			StudentClassroomSessionDivisionSubjectEvaluation studentClassroomSessionDivisionSubjectEvaluation = new StudentClassroomSessionDivisionSubjectEvaluation();
    			Student student = null;
    			for(Student index : students){
    				//System.out
					//		.println("AbstractCreateDatabaseBusinessIT.processPreviousDatabasesStudentEvaluations() : "+index.getCode()+" , "+values[0]);
    				if(index.getCode().equals(values[0])){
    					student = index;
    					break;
    				}
    			}
    			
    			if(student==null){
    				//System.out.println("No student found for registration code "+values[0]);
    				continue;
    			}
    			studentsExists.add(student);
    			for(StudentClassroomSessionDivisionSubject index : studentClassroomSessionDivisionSubjects){
    				if(index.getStudent().getCode().equals(student.getCode()) 
    						&& 	index.getClassroomSessionDivisionSubject().getClassroomSessionDivision().getOrderNumber().equals(divisionIndex)
    						&& index.getClassroomSessionDivisionSubject().getSubject().equals(getSubject(values[1],Boolean.FALSE))){
    					studentClassroomSessionDivisionSubjectEvaluation.setStudentSubject(index);
    					studentClassroomSessionDivisionSubjectEvaluation.setValue(new BigDecimal(values[3]));
    					break;
    				}
    			}
    			
    			if(studentClassroomSessionDivisionSubjectEvaluation.getStudentSubject()==null){
    				//System.out.println("No student subject found for registration code "+values[0]+" and subject "+values[1]);
    				continue;
    			}else
    				studentClassroomSessionDivisionSubjectsExists.add(studentClassroomSessionDivisionSubjectEvaluation.getStudentSubject());
    			
    			if(evaluation == null || !values[2].equals(previousEvaluationTypeCode)){
    				evaluation = new Evaluation();
    				evaluation.setClassroomSessionDivisionSubjectEvaluationType(inject(ClassroomSessionDivisionSubjectEvaluationTypeBusiness.class)
        					.findBySubjectByEvaluationType(studentClassroomSessionDivisionSubjectEvaluation.getStudentSubject().getClassroomSessionDivisionSubject(),
        							inject(EvaluationTypeBusiness.class).find(previousEvaluationTypeCode = values[2])));
    				
    				Evaluation previous = null;
    				for(Evaluation index : evaluations)
    					if(index.getClassroomSessionDivisionSubjectEvaluationType().equals(evaluation.getClassroomSessionDivisionSubjectEvaluationType())){
    						previous = index;
    						
    						System.out.println("Duplicate found for "+evaluation.getClassroomSessionDivisionSubjectEvaluationType().getClassroomSessionDivisionSubject().getSubject()
    								+" - "+evaluation.getClassroomSessionDivisionSubjectEvaluationType().getEvaluationType());
    						
    						break;
    					}
    				
    				if(previous==null)
    					evaluations.add(evaluation);
    			}
    			
    			studentClassroomSessionDivisionSubjectEvaluation.setEvaluation(evaluation);
    			evaluation.getStudentSubjectEvaluations().add(studentClassroomSessionDivisionSubjectEvaluation);
				
    		}
    		
    	}
    	
    	//System.out.println("Number of students : "+students.size());
    	//System.out.println("Number of students subjects : "+studentClassroomSessionDivisionSubjectsExists.size());
    	System.out.println("Number of evaluations : "+evaluations.size());
    	/*
    	for(StudentClassroomSessionDivision studentClassroomSessionDivision : studentClassroomSessionDivisions)
    		if(studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber().intValue()<2)
    			System.out.println("No student class room session division has been found for : "+studentClassroomSessionDivision);
    	
    	System.out.println("Updating "+updatedStudentClassroomSessionDivisions.size()+" student class room session divisions");
		inject(Instance().getStudentClassroomSessionDivisionBusiness.class).update(updatedStudentClassroomSessionDivisions);	
		*/
    	inject(EvaluationBusiness.class).create(evaluations);
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
    
    private Subject getSubject(String code,Boolean load){
    	code = StringUtils.trim(code);
    	String icode = code;
    	if(Boolean.TRUE.equals(load)){
	    	if(code.equals("S17"))
	    		code = IesaConstant.SUBJECT_GRAMMAR_CODE;
	    	else if(code.equals("S21"))
	    		code = IesaConstant.SUBJECT_FRENCH_CODE;
    	}else{
	    	for(Subject subject : subjects){
	    		if(subject.getName().equalsIgnoreCase(code)){
	    			code = subject.getCode();
	    			//System.out.println("Found s c : "+code);
	    			//System.exit(0);
	    		}
	    		if(StringUtils.isNotBlank(code))
	    			break;
	    	}
	    	
	    	code = icode;
	    	for(Subject subject : subjects){
	    		if(StringUtils.remove(subject.getName(), Constant.CHARACTER_SPACE).equalsIgnoreCase(code)){
	    			code = subject.getCode();
	    		}
	    		if(StringUtils.isNotBlank(code))
	    			break;
	    	}
	    	
	    	code = icode;
	    	if(code.equals("Mathematics"))
	    		code = IesaConstant.SUBJECT_MATHEMATICS_CODE;
	    	else if(code.equals("Spanish"))
	    		code = IesaConstant.SUBJECT_SPANISH_CODE;
	    	else if(code.equals("Biology"))
	    		code = IesaConstant.SUBJECT_BIOLOGY_CODE;
	    	else if(code.equals("Chemistry"))
	    		code = IesaConstant.SUBJECT_CHEMISTRY_CODE;
	    	else if(code.equals("French"))
	    		code = IesaConstant.SUBJECT_FRENCH_CODE;
	    	else if(code.equals("Physics"))
	    		code = IesaConstant.SUBJECT_PHYSICS_CODE;
	    	else if(code.equals("Grammar"))
	    		code = IesaConstant.SUBJECT_GRAMMAR_CODE;
	    	else if(code.equals("Reading&Comprehension"))
	    		code = IesaConstant.SUBJECT_READING_COMPREHENSION_CODE;
	    	else if(code.equals("Handwriting"))
	    		code = IesaConstant.SUBJECT_HANDWRITING_CODE;
	    	else if(code.equals("Spelling"))
	    		code = IesaConstant.SUBJECT_SPELLING_CODE;
	    	else if(code.equals("Phonics"))
	    		code = IesaConstant.SUBJECT_PHONICS_CODE;
	    	else if(code.equals("Creativewriting"))
	    		code = IesaConstant.SUBJECT_CREATIVE_WRITING_CODE;
	    	else if(code.equals("Moraleducation"))
	    		code = IesaConstant.SUBJECT_MORAL_EDUCATION_CODE;
	    	else if(code.equals("SocialStudies"))
	    		code = IesaConstant.SUBJECT_SOCIAL_STUDIES_CODE;
	    	else if(code.equals("Science"))
	    		code = IesaConstant.SUBJECT_SCIENCE_CODE;
	    	else if(code.equals("French"))
	    		code = IesaConstant.SUBJECT_FRENCH_CODE;
	    	else if(code.equals("Art&Craft"))
	    		code = IesaConstant.SUBJECT_ART_CRAFT_CODE;
	    	else if(code.equals("Music"))
	    		code = IesaConstant.SUBJECT_MUSIC_CODE;
	    	else if(code.equals("ICT"))
	    		code = IesaConstant.SUBJECT_ICT_COMPUTER_CODE;
	    	else if(code.equals("Physicaleducation"))
	    		code = IesaConstant.SUBJECT_PHYSICAL_EDUCATION_CODE;
	    	else if(code.equals("Literatureinenglish"))
	    		code = IesaConstant.SUBJECT_LITERATURE_IN_ENGLISH_CODE;
	    	else if(code.equals("Geography"))
	    		code = IesaConstant.SUBJECT_GEOGRAPHY_CODE;
	    	else if(code.equals("History"))
	    		code = IesaConstant.SUBJECT_HISTORY_CODE;
	    	else if(code.equals("EnglishLanguage"))
	    		code = IesaConstant.SUBJECT_ENGLISH_LANGUAGE_CODE;
	    	else if(code.equals("Comprehension"))
	    		code = IesaConstant.SUBJECT_COMPREHENSION_CODE;
	    	else if(code.equals("Litterature"))
	    		code = IesaConstant.SUBJECT_LITERATURE_CODE;
	    	
	    		
    	}
    	
    	/*
    	S01	MATHEMATICS
    	S02	GRAMMAR
    	S03	READING & COMPREHENSION
    	S04	HANDWRITING
    	S05	SPELLING
    	S06	PHONICS
    	S07	CREATIVE WRITING
    	S08	MORAL EDUCATION
    	S09	SOCIAL STUDIES
    	S10	SCIENCE
    	S11	FRENCH
    	S12	ART & CRAFT
    	S13	MUSIC
    	S14	ICT (Computer)
    	S15	PHYSICAL EDUCATION

    	S17	GRAMMAR
    	S18	LITERATURE
    	S19	COMPREHENSION
    	S20	HISTORY
    	S21	FRENCH

    	S22	ENGLISH LANGUAGE
    	S23	LITERATURE IN ENGLISH
    	S24	GEOGRAPHY
    	S25	PHYSICS
    	S26	CHEMISTRY
    	S27	BIOLOGY
    	S28	SPANISH
    	S29	ENGLISH LITERATURE
    	S30	CHINESE MANDARIN
    	S31	ACCOUNTING
    	S32	BUSINESS STUDIES
    	S33	ECONOMICS
    	S34	ADVANCED MATHEMATICS
    	S35	CORE MATHEMATICS
    	S36	ART & DESIGN
    	S37	DEVELOPMENT STUDIES
    	S38	ENVIRONMENTAL MANAGEMENT

    	S39	SOCIOLOGY
    	S40	LAW
		*/
    	Subject subject = inject(SubjectBusiness.class).find(code);
    	if(subject==null)
    		System.out.println("No subject has code "+icode);
    	else{
    		/*if(subject.getName().toLowerCase().startsWith("science"))
    			System.out.println("science : "+icode+" > "+code);
    		
    		if(subject.getName().toLowerCase().startsWith("litterature"))
    			System.out.println("litterature : "+icode+" > "+code);
    		*/
    	}
    	
    	
    	return subject;
    }

    protected Boolean isSimulated(){
    	return Boolean.FALSE;
    }
}
