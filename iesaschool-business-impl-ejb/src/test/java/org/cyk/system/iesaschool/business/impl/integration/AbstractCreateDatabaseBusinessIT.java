package org.cyk.system.iesaschool.business.impl.integration;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.school.business.api.actor.TeacherBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.StudentClassroomSessionDivisionBusiness;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.system.school.persistence.api.subject.SubjectDao;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;
import org.cyk.utility.common.Constant;

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
    
    private void processTeachersSheet(File file,final File signatureDirectory) throws Exception{}
    
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
    
    private void processStudentsSheet(final String levelNameCode,final String classroomSessionSuffix,File file,final File imageDirectory,Integer count) throws Exception{}
    
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
