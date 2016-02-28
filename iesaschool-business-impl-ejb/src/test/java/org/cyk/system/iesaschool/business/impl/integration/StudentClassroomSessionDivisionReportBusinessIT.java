package org.cyk.system.iesaschool.business.impl.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.EvaluationType;

public class StudentClassroomSessionDivisionReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	installApplication();
    	
    	Collection<ClassroomSession> classroomSessions = SchoolBusinessLayer.getInstance().getClassroomSessionBusiness().findAll();
    	ClassroomSession pk=getClassroomSessions(0).iterator().next(),k1=getClassroomSessions(1).iterator().next(),k2=getClassroomSessions(2).iterator().next()
    			,k3=getClassroomSessions(3).iterator().next()
    			,g1=getClassroomSessions(4).iterator().next(),g2=getClassroomSessions(5).iterator().next(),g3=getClassroomSessions(6).iterator().next()
    			,g4=getClassroomSessions(7).iterator().next(),g5=getClassroomSessions(8).iterator().next(),g6=getClassroomSessions(9).iterator().next()
    			,g7=getClassroomSessions(10).iterator().next(),g8=getClassroomSessions(11).iterator().next(),g9=getClassroomSessions(12).iterator().next()
    			,g10=getClassroomSessions(13).iterator().next(),g11=getClassroomSessions(14).iterator().next(),g12=getClassroomSessions(15).iterator().next();
    	
    	/*
    	for(ClassroomSession classroomSession : classroomSessions)
    		if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G1) && g1 == null )
    			g1 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G2) && g2 == null )
    			g2 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G3) && g3 == null )
    			g3 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G4) && g4 == null )
    			g4 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G5) && g5 == null )
    			g5 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G6) && g6 == null )
    			g6 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G7) && g7 == null )
    			g7 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G8) && g8 == null )
    			g8 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G9) && g9 == null )
    			g9 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G10) && g10 == null )
    			g10 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G11) && g11 == null )
    			g11 = classroomSession;
    		else if(classroomSession.getLevelTimeDivision().getLevel().getName().getCode().equals(IesaConstant.LEVEL_NAME_CODE_G12) && g12 == null )
    			g12 = classroomSession;
    	*/
    	schoolBusinessTestHelper.createActors(Student.class,new String[]{"STUD1","STUD2","STUD3","STUD4","STUD5"});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g1, new Object[][]{{15},{15},{15}}); 
    	
    	//schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g9, new Object[][]{{15},{15},{15}}); 
    	
    	//schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"},g3, new Object[][]{{15},{15},{15}}); 
    	
    	schoolBusinessTestHelper.getEvaluationTypes().addAll(rootDataProducerHelper.getEnumerations(EvaluationType.class));
    	
    	ClassroomSessionDivision classroomSessionDivision = SchoolBusinessLayer.getInstance().getClassroomSessionDivisionBusiness().findByClassroomSession(g1).iterator().next();
    	List<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects = new ArrayList<>(SchoolBusinessLayer.getInstance().getClassroomSessionDivisionSubjectBusiness().findByClassroomSessionDivision(classroomSessionDivision));
    	
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","90","30","60"}
    	    		//,{"STUD2","70","50","60"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE);
    	
    	/*
    	classroomSessionDivision = SchoolBusinessLayer.getInstance().getClassroomSessionDivisionBusiness().findByClassroomSession(g9).iterator().next();
    	classroomSessionDivisionSubjects = new ArrayList<>(SchoolBusinessLayer.getInstance().getClassroomSessionDivisionSubjectBusiness().findByClassroomSessionDivision(classroomSessionDivision));
    	schoolBusinessTestHelper.simulateStudentClassroomSessionDivisionReport(classroomSessionDivision, new Object[][]{
    		new Object[]{classroomSessionDivisionSubjects.get(0),new String[][]{
    	    		{"STUD1","90","30","60"}
    	    		,{"STUD2","70","50","60"}
    	    	}}
    	}, Boolean.TRUE,Boolean.TRUE);
    	*/
    }
        
}
