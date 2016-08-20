package org.cyk.system.iesaschool.business.impl.integration;

import java.util.ArrayList;
import java.util.List;

import org.cyk.system.school.business.api.session.ClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.EvaluationType;

public class StudentClassroomSessionDivisionReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    /*private */ClassroomSession pk,k1,k2,k3,g1,g2,g3,g4,g5,g6,g7,g8,g9,g10,g11,g12;
    
    @Override
    protected void businesses() {  
    	installApplication();
    	
    	pk=getClassroomSessions(0).iterator().next();
    	k1=getClassroomSessions(1).iterator().next();
    	k2=getClassroomSessions(2).iterator().next();
    	k3=getClassroomSessions(3).iterator().next();
    	g1=getClassroomSessions(4).iterator().next();
    	g2=getClassroomSessions(5).iterator().next();
    	g3=getClassroomSessions(6).iterator().next();
    	g4=getClassroomSessions(7).iterator().next();
    	g5=getClassroomSessions(8).iterator().next();
    	g6=getClassroomSessions(9).iterator().next();
    	g7=getClassroomSessions(10).iterator().next();
    	g8=getClassroomSessions(11).iterator().next();
    	g9=getClassroomSessions(12).iterator().next();
    	g10=getClassroomSessions(13).iterator().next();
    	g11=getClassroomSessions(14).iterator().next();
    	g12=getClassroomSessions(15).iterator().next();
    	
    	
    	schoolBusinessTestHelper.createActors(Student.class,new String[]{"STUD1","STUD2","STUD3","STUD4","STUD5"});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},pk, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},k1, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},k2, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},k3, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g1, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g4, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g7, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g9, new Object[][]{{15},{15},{15}}); 
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g12, new Object[][]{{15},{15},{15}}); 
    	
    	schoolBusinessTestHelper.getEvaluationTypes().addAll(rootDataProducerHelper.getEnumerations(EvaluationType.class));
    	
    	
    	
    	/*
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSession(pk).iterator().next();
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, null, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSession(k1).iterator().next();
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, null, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSession(k2).iterator().next();
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, null, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSession(k3).iterator().next();
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, null, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	*/
    	
    	trimester(new Byte("0"));
    	trimester(new Byte("1"));
    	trimester(new Byte("2"));
    }
    
    private void trimester(Byte index){
    	ClassroomSessionDivision classroomSessionDivision;
    	List<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects;
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionByIndex(g1,index);
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","85","20","30"}
    	    		,{"STUD2","55","50","60"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionByIndex(g4,index);
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","84","12","23"}
    	    		,{"STUD2","51","94","26"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	
    	classroomSessionDivision = inject(ClassroomSessionDivisionBusiness.class).findByClassroomSessionByIndex(g7,index);
    	classroomSessionDivisionSubjects = new ArrayList<>(inject(ClassroomSessionDivisionSubjectBusiness.class).findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","48","15","26"}
    	    		,{"STUD2","24","35","68"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
    	
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
