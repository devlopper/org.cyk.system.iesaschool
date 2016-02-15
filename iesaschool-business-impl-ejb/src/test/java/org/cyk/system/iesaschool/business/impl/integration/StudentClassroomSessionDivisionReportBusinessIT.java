package org.cyk.system.iesaschool.business.impl.integration;

import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.SchoolBusinessTestHelper.ClassroomSessionDivisionInfos;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.session.ClassroomSession;

public class StudentClassroomSessionDivisionReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
 
    @Override
    protected void businesses() {
    	installApplication();
    	
    	Collection<ClassroomSession> classroomSessions = SchoolBusinessLayer.getInstance().getClassroomSessionBusiness().findAll();
    	ClassroomSession g1,g2,g3,g4,g5,g6,g7,g8,g9,g10,g11,g12;
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
    	
    	schoolBusinessTestHelper.createActors(Student.class,new String[]{"STUD1","STUD2","STUD3","STUD4","STUD5"});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"/*,"STUD3","STUD4","STUD5"*/},g1, new Object[][]{{15},{15},{15}}); 
    	
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"/*,"STUD3","STUD4","STUD5"*/},g2, new Object[][]{{15},{15},{15}}); 
    	
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"STUD1","STUD2"/*,"STUD3","STUD4","STUD5"*/},g3, new Object[][]{{15},{15},{15}}); 
    	
    	schoolBusinessTestHelper.getEvaluationTypes().addAll(dataProducer.getEvaluationTypes());
    	
    	//trimesterEverybodyHaveAllEvaluations(dataProducer.getGrade1().division(0),Boolean.TRUE,Boolean.TRUE);
    	
    	trimesterEverybodyHaveNotAllEvaluations(dataProducer.getGrade1().division(0),Boolean.TRUE,Boolean.TRUE);
    	trimesterEverybodyHaveNotAllEvaluations(dataProducer.getGrade2().division(0),Boolean.TRUE,Boolean.TRUE);
    }
    
    private void trimesterEverybodyHaveNotAllEvaluations(ClassroomSessionDivisionInfos classroomSessionDivisionInfos,Boolean generateReport,Boolean printReport){
    	schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionInfos.subject(0).getClassroomSessionDivisionSubject(),new String[][]{
    		{"STUD1","90","30","60"}
    		,{"STUD2","70","50","60"}
              /*,{"STUD3","40","60","40"}
              ,{"STUD4","45","45","80"}
              ,{"STUD5","20","95","55"}*/
    	});
    	
    	schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionInfos.subject(1).getClassroomSessionDivisionSubject(),new String[][]{{
    		"STUD1",null,"50","70"}
    		,{"STUD2","90","15","65"}
              /*,{"STUD3","40","60","40"}
              ,{"STUD4","45","45","80"}
              ,{"STUD5","20","95","55"}*/
    	});
    	
    	schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionInfos.subject(2).getClassroomSessionDivisionSubject(),new String[][]{{
    		"STUD1",null,null,"70"}
    		,{"STUD2","45","50","50"}
              /*,{"STUD3","40","60","40"}
              ,{"STUD4","45","45","80"}
              ,{"STUD5","20","95","55"}*/
    	});
    	
    	schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionInfos.subject(3).getClassroomSessionDivisionSubject(),new String[][]{{
    		"STUD1",null,null,null}
    		,{"STUD2","80","30","75"}
              /*,{"STUD3","40","60","40"}
              ,{"STUD4","45","45","80"}
              ,{"STUD5","20","95","55"}*/
    	});
    	
    	schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionInfos.subject(4).getClassroomSessionDivisionSubject(),new String[][]{{
    		"STUD1","50",null,"70"}
    		,{"STUD2","55","75","60"}
             /* ,{"STUD3","40","60","40"}
              ,{"STUD4","45","45","80"}
              ,{"STUD5","20","95","55"}*/
    	});
    	
    	schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionInfos.subject(5).getClassroomSessionDivisionSubject(),new String[][]{{
    		"STUD1","50","70",null}
    		,{"STUD2","80","80","40"}
              /*,{"STUD3","40","60","40"}
              ,{"STUD4","45","45","80"}
              ,{"STUD5","20","95","55"}*/
    	});
    	
    	schoolBusinessTestHelper.createSubjectEvaluations(classroomSessionDivisionInfos.subject(6).getClassroomSessionDivisionSubject(),new String[][]{{
    		"STUD1",null,null,"70"}
    		,{"STUD2","50","50","50"}
              /*,{"STUD3","40","60","40"}
              ,{"STUD4","45","45","80"}
              ,{"STUD5","20","95","55"}*/
    	});

    	 
    	if(Boolean.TRUE.equals(generateReport)){
    		schoolBusinessTestHelper.randomValues(Arrays.asList(classroomSessionDivisionInfos.getClassroomSessionDivision()),Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
    		schoolBusinessTestHelper.createStudentClassroomSessionDivisionReport(Arrays.asList(classroomSessionDivisionInfos.getClassroomSessionDivision()),printReport);
    	}
    }
    
}
