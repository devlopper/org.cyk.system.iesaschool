package org.cyk.system.iesaschool.business.impl.integration;

import java.util.Arrays;

import org.cyk.system.root.business.api.geography.ElectronicMailBusiness;
import org.cyk.system.root.model.party.person.PersonRelationshipType;
import org.cyk.system.school.business.api.actor.StudentBusiness;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.subject.EvaluationType;

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
    	student1.setCode("G1_STUD1");
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
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"K1_STUD1"},k1, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"K2_STUD1"},k2, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"K3_STUD1"},k3, new Object[][]{{0},{0},{0}});
    	schoolBusinessTestHelper.createStudentClassroomSessions(new String[]{"G1_STUD1"},g1, new Object[][]{{15},{15},{15}});
    	
    	schoolBusinessTestHelper.getEvaluationTypes().addAll(rootDataProducerHelper.getEnumerations(EvaluationType.class));
    	
    	schoolBusinessTestHelper.generateStudentClassroomSessionDivisionReport(new Object[][]{
        		{SchoolConstant.Code.LevelName.PK,null,1l}
        		,{SchoolConstant.Code.LevelName.K1,null,1l}
        		,{SchoolConstant.Code.LevelName.K2,null,1l}
        		,{SchoolConstant.Code.LevelName.K3,null,1l}
        		,{SchoolConstant.Code.LevelName.G1,"A",1l}
        	}, Boolean.TRUE, Boolean.FALSE);

    }
        
}
