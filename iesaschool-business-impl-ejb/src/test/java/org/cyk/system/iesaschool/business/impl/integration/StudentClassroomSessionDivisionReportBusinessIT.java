package org.cyk.system.iesaschool.business.impl.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cyk.system.root.business.api.geography.ElectronicMailBusiness;
import org.cyk.system.root.model.party.person.PersonRelationshipType;
import org.cyk.system.school.business.api.actor.StudentBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.LevelTimeDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.EvaluationType;
import org.cyk.system.school.persistence.api.session.LevelTimeDivisionDao;

public class StudentClassroomSessionDivisionReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    /*private */ClassroomSession pk,k1,k2,k3,g1,g2,g3,g4,g5,g6,g7,g8,g9,g10,g11,g12;
    
    @Override
    protected void businesses() {  
    	installApplication();
    	
    	pk=getClassroomSessionOne(0l);
    	k1=getClassroomSessionOne(1l);
    	k2=getClassroomSessionOne(2l);
    	k3=getClassroomSessionOne(3l);
    	g1=getClassroomSessionOne(4l);
    	g2=getClassroomSessionOne(5l);
    	g3=getClassroomSessionOne(6l);
    	g4=getClassroomSessionOne(7l);
    	g5=getClassroomSessionOne(8l);
    	g6=getClassroomSessionOne(9l);
    	g7=getClassroomSessionOne(10l);
    	g8=getClassroomSessionOne(11l);
    	g9=getClassroomSessionOne(12l);
    	g10=getClassroomSessionOne(13l);
    	g11=getClassroomSessionOne(14l);
    	g12=getClassroomSessionOne(15l);
    	
    	
    	Student student1 = inject(StudentBusiness.class).instanciateOne();
    	student1.setCode("STUD1");
    	student1.setName("komenan");
    	student1.getPerson().setLastnames("yao christian");
    	inject(ElectronicMailBusiness.class).setAddress(student1.getPerson(), PersonRelationshipType.FAMILY_FATHER, "kycdev@gmail.com");
    	inject(ElectronicMailBusiness.class).setAddress(student1.getPerson(), PersonRelationshipType.FAMILY_MOTHER, "ckydevbackup@gmail.com");
    	
    	Student pkStudent = inject(StudentBusiness.class).instanciateOne();
    	pkStudent.setCode("PK_STUD1");
    	pkStudent.setName("Zadi");
    	pkStudent.getPerson().setLastnames("leon");
    	//inject(ElectronicMailBusiness.class).setAddress(pkStudent.getPerson(), PersonRelationshipType.FAMILY_FATHER, "kycdev@gmail.com");
    	//inject(ElectronicMailBusiness.class).setAddress(pkStudent.getPerson(), PersonRelationshipType.FAMILY_MOTHER, "ckydevbackup@gmail.com");
    	
    	Student k1Student = inject(StudentBusiness.class).instanciateOne();
    	k1Student.setCode("K1_STUD1");
    	k1Student.setName("Kacou");
    	k1Student.getPerson().setLastnames("philipe");
    	
    	Student k2Student = inject(StudentBusiness.class).instanciateOne();
    	k2Student.setCode("K2_STUD1");
    	k2Student.setName("Anza");
    	k2Student.getPerson().setLastnames("roger");
    	
    	Student k3Student = inject(StudentBusiness.class).instanciateOne();
    	k3Student.setCode("K3_STUD1");
    	k3Student.setName("Aka");
    	k3Student.getPerson().setLastnames("clarisse");
    	
    	inject(StudentBusiness.class).create(Arrays.asList(pkStudent,k1Student,k2Student,k3Student,student1));
    	
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"PK_STUD1"},pk, new Object[][]{{0},{0},{0}});
    	/*schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},k1, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},k2, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},k3, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g1, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g4, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g7, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g9, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g12, new Object[][]{{15},{15},{15}}); 
    	*/
    	schoolBusinessTestHelper.getEvaluationTypes().addAll(rootDataProducerHelper.getEnumerations(EvaluationType.class));
    	
    	
    	LevelTimeDivision levelTimeDivision = inject(LevelTimeDivisionDao.class).readByGlobalIdentifierOrderNumber(0l).iterator().next();
    	ClassroomSessionDivision classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionsByOrderNumber(
    			inject(ClassroomSessionBusiness.class).findByLevelTimeDivision(levelTimeDivision), 1l).iterator().next();
    	
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, null
    			,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);
    	
    	//trimester(new Long("1"));
    	/*trimester(new Long("1"));
    	trimester(new Long("2"));*/
    }
    
    private void trimester(Long index){
    	ClassroomSessionDivision classroomSessionDivision;
    	List<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects;
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionByOrderNumber(g1,index);
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","85","20","30"}
    	    		,{"STUD2","55","50","60"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionByOrderNumber(g4,index);
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","84","12","23"}
    	    		,{"STUD2","51","94","26"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionByOrderNumber(g7,index);
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","48","15","26"}
    	    		,{"STUD2","24","35","68"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);
    	
    	/*
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionByIndex(g9,index);
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","90","30","60"}
    	    		,{"STUD2","70","50","60"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	*/
    	
    }
        
}
