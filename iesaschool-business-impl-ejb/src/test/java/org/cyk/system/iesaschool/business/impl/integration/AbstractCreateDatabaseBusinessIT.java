package org.cyk.system.iesaschool.business.impl.integration;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.IdentifiableBusinessService.CompleteInstanciationOfOneFromValuesListener;
import org.cyk.system.root.business.api.file.FileBusiness;
import org.cyk.system.root.business.api.geography.ElectronicMailBusiness;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness.CompleteActorInstanciationOfManyFromValuesArguments;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.party.person.PersonRelationshipType;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.root.model.party.person.Sex;
import org.cyk.system.school.business.api.actor.StudentBusiness;
import org.cyk.system.school.business.api.actor.TeacherBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.StudentClassroomSessionDivisionBusiness;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.system.school.persistence.api.actor.StudentDao;
import org.cyk.system.school.persistence.api.actor.TeacherDao;
import org.cyk.system.school.persistence.api.subject.SubjectDao;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.file.ExcelSheetReader;

public abstract class AbstractCreateDatabaseBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
     
    @Override
    protected void businesses() {
   
    	if(Boolean.TRUE.equals(noData())){
    		System.exit(0);
    		return;
    	}
    	File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data");
		File excelWorkbookFile = new File(directory, "2016_2017_Trimester_1.xls")
			,teachersSignatureDirectory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\20162017\\signature")
			,studentsPhotoDirectory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\20162017\\photo");
		
    	try {
			processTeachersSheet(excelWorkbookFile,teachersSignatureDirectory);
			processCoordinatorsSheet(excelWorkbookFile);
			processClassroomSessionDivisionSubjectTeachersSheet(excelWorkbookFile);
			
			processStudentsSheet(SchoolConstant.Code.LevelName.PK,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.K1,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.K2,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.K3,"A",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.K3,"B",excelWorkbookFile,studentsPhotoDirectory,null);
			
			processStudentsSheet(SchoolConstant.Code.LevelName.G1,"A",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G1,"B",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G2,"A",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G2,"B",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G3,"A",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G3,"B",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G4,"A",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G4,"B",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G5,"A",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G5,"B",excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G6,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G7,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G8,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G9,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G10,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G11,null,excelWorkbookFile,studentsPhotoDirectory,null);
			processStudentsSheet(SchoolConstant.Code.LevelName.G12,null,excelWorkbookFile,studentsPhotoDirectory,null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.exit(0);
    }
    
    protected Boolean noData(){
    	return Boolean.FALSE;
    }
    
    private void processTeachersSheet(File file,final File signatureDirectory) throws Exception{
    	ExcelSheetReader excelSheetReader = null;//new ExcelSheetReader.Adapter.Default();
    	excelSheetReader.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	excelSheetReader.setIndex(1);
    	excelSheetReader.setFromRowIndex(35);
    	excelSheetReader.setFromColumnIndex(0);
    	excelSheetReader.setRowCount(36);
    	
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(1);
    	readExcelSheetArguments.setFromRowIndex(35);
    	readExcelSheetArguments.setFromColumnIndex(0);
    	readExcelSheetArguments.setRowCount(36);
    	readExcelSheetArguments.setListener(new ReadExcelSheetArguments.Listener.Adapter.Default(){
			private static final long serialVersionUID = 1L;
    		@Override
    		public Boolean addable(String[] row) {
    			return inject(TeacherDao.class).read(row[1])==null;
    		}
    	});
    	
		CompleteActorInstanciationOfManyFromValuesArguments<Teacher> completeActorInstanciationOfManyFromValuesArguments = new CompleteActorInstanciationOfManyFromValuesArguments<>();
		
		completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setRegistrationCodeIndex(1);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setTitleCodeIndex(3);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setNameIndex(6);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setLastnameIndex(5);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setListener(new CompleteInstanciationOfOneFromValuesListener<Teacher>() {
			@Override
			public void beforeProcessing(Teacher teacher,String[] values) {
				values[3] = getPersonTitleCode(values[3]);
			}
    		@Override
			public void afterProcessing(Teacher teacher,String[] values) {
    			/*if(teacher.getPerson().getExtendedInformations()==null)
    				teacher.getPerson().setExtendedInformations(new PersonExtendedInformations(teacher.getPerson()));
    			if(teacher.getPerson().getExtendedInformations()==null)
    				teacher.getPerson().setExtendedInformations(new PersonExtendedInformations(teacher.getPerson()));
    			teacher.getPerson().getExtendedInformations().getTitle().setCode(getPersonTitleCode(teacher.getPerson().getExtendedInformations().getTitle().getCode()));
    			*/
				File signatureFile = new File(signatureDirectory,new BigDecimal(values[0]).intValue()+".png");
				if(!signatureFile.exists())
					signatureFile = new File(signatureDirectory,new BigDecimal(values[0]).intValue()+".jpg");
				
				if(signatureFile.exists())
					try {
						teacher.getPerson().getExtendedInformations().setSignatureSpecimen(inject(FileBusiness.class)
							.process(IOUtils.toByteArray(new FileInputStream(signatureFile)), teacher.getCode()+" signature.png"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				else
					System.out.println("Signature file not found for "+teacher.getCode()+" : "+signatureFile.getName());
			}
		});
    
    	System.out.print("Instanciating teachers");
    	List<Teacher> teachers = inject(TeacherBusiness.class).instanciateMany(excelSheetReader, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.println(" - Creating "+teachers.size()+" teachers");
    	inject(GenericBusiness.class).create(CommonUtils.getInstance().castCollection(teachers, AbstractIdentifiable.class));
    }
    
    private void processCoordinatorsSheet(File file) throws Exception{
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(3);
    	readExcelSheetArguments.setFromRowIndex(0);
		List<String[]> list = CommonUtils.getInstance().readExcelSheet(readExcelSheetArguments);
    	Collection<ClassroomSession> classroomSessions = new ArrayList<>();
		for(String[] values : list){
    		ClassroomSession classroomSession = getClassroomSession(values[0]);
    		if(classroomSession==null){
    			System.out.println("No classroom session found for coordinator "+values[1]);
    			continue;
    		}
    		classroomSession.setCoordinator(inject(TeacherBusiness.class).find(values[1]));
    		classroomSessions.add(classroomSession);
    	}
		System.out.println("Updating "+classroomSessions.size()+" class room sessions");
		inject(GenericBusiness.class).update(CommonUtils.getInstance().castCollection(classroomSessions, AbstractIdentifiable.class));
    }
    
    private void processClassroomSessionDivisionSubjectTeachersSheet(File file) throws Exception{
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(2);
    	readExcelSheetArguments.setFromRowIndex(0);
		List<String[]> list = CommonUtils.getInstance().readExcelSheet(readExcelSheetArguments);
    	Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects = new ArrayList<>();
		for(String[] values : list){
			for(ClassroomSessionDivisionSubject classroomSessionDivisionSubject : classroomSessionDivisionSubjectDao
    				.readByClassroomSessionBySubject(getClassroomSession(values[0]),getSubject(values[1]))){
    			
    			classroomSessionDivisionSubject.setTeacher(inject(TeacherBusiness.class).find(values[2]));
    			classroomSessionDivisionSubjects.add(classroomSessionDivisionSubject);
    		}
    	}
		System.out.println("Updating "+classroomSessionDivisionSubjects.size()+" class room session division subjects");
		inject(GenericBusiness.class).update(CommonUtils.getInstance().castCollection(classroomSessionDivisionSubjects, AbstractIdentifiable.class));
    }
    
    private void processStudentsSheet(final String levelNameCode,final String classroomSessionSuffix,File file,final File imageDirectory,Integer count) throws Exception{
    	Collection<ClassroomSession> classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(levelNameCode, classroomSessionSuffix);
    	final ClassroomSession classroomSession = classroomSessions.isEmpty() ? null : classroomSessions.iterator().next();
    	final Collection<String> studentCodes = new ArrayList<>();
    	
    	ExcelSheetReader excelSheetReader = null;//new ExcelSheetReader.Adapter.Default();
    	excelSheetReader.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	excelSheetReader.setIndex(0);
    	excelSheetReader.setFromRowIndex(1);
    	excelSheetReader.setFromColumnIndex(0);
    	excelSheetReader.setRowCount(count);
    	
    	ReadExcelSheetArguments readExcelSheetArguments = new ReadExcelSheetArguments();
    	readExcelSheetArguments.setWorkbookBytes(IOUtils.toByteArray(new FileInputStream(file)));
    	readExcelSheetArguments.setSheetIndex(0);
    	readExcelSheetArguments.setFromRowIndex(1);
    	readExcelSheetArguments.setFromColumnIndex(0);
    	readExcelSheetArguments.setRowCount(count);
    	
    	readExcelSheetArguments.setListener(new ReadExcelSheetArguments.Listener.Adapter.Default(){
			private static final long serialVersionUID = 1L;
    		@Override
    		public Boolean addable(String[] row) {
    			return inject(StudentDao.class).read(row[1])==null && isLevel(levelNameCode, classroomSessionSuffix, row[7]);
    		}
    	});
    	
		CompleteActorInstanciationOfManyFromValuesArguments<Student> completeActorInstanciationOfManyFromValuesArguments = new CompleteActorInstanciationOfManyFromValuesArguments<>();
		
		completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setRegistrationCodeIndex(1);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setNameIndex(3);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setLastnameIndex(2);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().getPartyInstanciationOfOneFromValuesArguments().setBirthDateIndex(4);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setBirthLocationOtherDetailsIndex(6);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().getPersonInstanciationOfOneFromValuesArguments().setSexCodeIndex(8);
    	completeActorInstanciationOfManyFromValuesArguments.getInstanciationOfOneFromValuesArguments().setListener(new CompleteInstanciationOfOneFromValuesListener<Student>() {
			@Override
			public void beforeProcessing(Student student,String[] values) {
				//if(!ArrayUtils.contains(new String[]{"g9","g10","g11","g12"}, values[7].toLowerCase()));
				//	studentClassroomSessions.add(new StudentClassroomSession(student, getClassroomSession(values[7])));
			}
    		@Override
			public void afterProcessing(Student student,String[] values) {
    			if(student.getPerson().getSex()!=null)
    				student.getPerson().getSex().setCode(getSexCode(student.getPerson().getSex().getCode()));
    			if(StringUtils.isNotBlank(values[12]))
    				inject(ElectronicMailBusiness.class).setAddress(student.getPerson(), RootConstant.Code.PersonRelationshipType.FAMILY_FATHER, values[12]);
    			if(StringUtils.isNotBlank(values[17]))
    				inject(ElectronicMailBusiness.class).setAddress(student.getPerson(), RootConstant.Code.PersonRelationshipType.FAMILY_MOTHER, values[17]);
    			File photoFile = new File(imageDirectory,new BigDecimal(values[0]).intValue()+".jpg");
				if(!photoFile.exists())
					photoFile = new File(imageDirectory,new BigDecimal(values[0]).intValue()+".png");
				
    			if(photoFile.exists())
					try {
						student.setImage(inject(FileBusiness.class).process(IOUtils.toByteArray(new FileInputStream(photoFile)), student.getCode()+" photo.jpeg"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				else
					System.out.println("Photo not found for "+student.getCode()+" : "+photoFile.getName());
				
				if(classroomSession!=null && !ArrayUtils.contains(new String[]{"g9","g10","g11","g12"}, values[7].toLowerCase()));
					student.setStudentClassroomSession(new StudentClassroomSession(student, classroomSession));
			}
    		
		});
    	System.out.print(classroomSession);
    	System.out.print(" - Instanciating students");
    	List<Student> students = inject(StudentBusiness.class).instanciateMany(excelSheetReader, completeActorInstanciationOfManyFromValuesArguments);
    	System.out.print(" - "+students.size());
    	Collection<Student> existing = inject(StudentDao.class).read(studentCodes);
    	for(int i = 0; i< students.size();){
    		Boolean found = false;
    		for(Student e : existing)
    			if(students.get(i).getCode().equals(e.getCode())){
    				students.remove(i);
    				found = true;
    				break;
    			}
    		if(!found)
    			i++;
    	}
    	
    	System.out.println(" - Creating "+students.size()+" students");
    	inject(GenericBusiness.class).create(CommonUtils.getInstance().castCollection(students, AbstractIdentifiable.class));
    	genericBusiness.flushEntityManager();
    	/*if(classroomSession.getLevelTimeDivision().getOrderNumber()<=11){
    		System.out.println(" - Creating "+studentClassroomSessions.size()+" student classroom sessions");
    		inject(StudentClassroomSessionBusiness.class).create(studentClassroomSessions);
    		genericBusiness.flushEntityManager();
    	}else
    		System.out.println();
    	*/
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
    
    
    
    private String getPersonTitleCode(String code){
    	if(code.equals("Mademoiselle"))
			return RootConstant.Code.PersonTitle.MISS;
		else if(code.equals("Madame"))
			return RootConstant.Code.PersonTitle.MADAM;
		else if(code.equals("Monsieur"))
			return RootConstant.Code.PersonTitle.MISTER;
    	return code;
    }
    
    private String getSexCode(String code){
    	if(code.equals("M"))
			return RootConstant.Code.Sex.MALE;
		else if(code.equals("F"))
			return RootConstant.Code.Sex.FEMALE;
    	return null;
    }
    
    private ClassroomSession getClassroomSession(String code){
    	code = StringUtils.replace(code, Constant.CHARACTER_SPACE.toString(), Constant.EMPTY_STRING).toLowerCase();
    	Collection<ClassroomSession> classroomSessions = null;
    	if(code.equals("pre-k") || code.equals("pk"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.PK, null);
    	else if(code.equals("kg1"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.K1, null);
    	else if(code.equals("kg2"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.K2, null);
    	else if(code.equals("kg3"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.K3, null);
    	else if(code.equals("g1a"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G1, "A");
    	else if(code.equals("g1b"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G1, "B");
    	else if(code.equals("g2a"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G2, "A");
    	else if(code.equals("g2b"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G2, "B");
    	else if(code.equals("g3a"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G3, "A");
    	else if(code.equals("g3b"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G3, "B");
    	else if(code.equals("g4a"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G4, "A");
    	else if(code.equals("g4b"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G4, "B");
    	else if(code.equals("g5a"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G5, "A");
    	else if(code.equals("g5b"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G5, "B");
    	else if(code.equals("g6"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G6, null);
    	else if(code.equals("g7"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G7, null);
    	else if(code.equals("g8"))
    		classroomSessions = inject(ClassroomSessionBusiness.class).findByLevelNameBySuffix(SchoolConstant.Code.LevelName.G8, null);
    	
    	return classroomSessions == null || classroomSessions.isEmpty() ? null : classroomSessions.iterator().next();
    }
    
    private Subject getSubject(String code){
    	code = StringUtils.trim(code);
    	return inject(SubjectDao.class).read(code);
    }
    
    private Boolean isLevel(String levelNameCode,String classroomSessionSuffix,String value){
    	if(StringUtils.startsWith(value, "KG"))
    		value = "K"+value.substring(2);
    	String c = (levelNameCode+StringUtils.defaultString(classroomSessionSuffix));
    	return StringUtils.equalsIgnoreCase(c, value);
    }

    protected Boolean isSimulated(){
    	return Boolean.FALSE;
    }
}
