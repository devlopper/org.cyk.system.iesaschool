package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.school.business.impl.session.AbstractStudentClassroomSessionDivisionSubjectDetails;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.subject.StudentClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.StudentClassroomSessionDivisionSubjectEvaluation;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;

public class StudentClassroomSessionDivisionSubjectDetails extends AbstractStudentClassroomSessionDivisionSubjectDetails implements Serializable{
	
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText @Sequence(direction=Direction.AFTER,field=FIELD_CLASSROOM_SESSION_DIVISION_SUBJECT) private String test1;
	@Input @InputText @Sequence(direction=Direction.AFTER,field=FIELD_TEST1) private String test2;
	@Input @InputText @Sequence(direction=Direction.AFTER,field=FIELD_TEST2) private String exam;
	
	public StudentClassroomSessionDivisionSubjectDetails(StudentClassroomSessionDivisionSubject studentClassroomSessionDivisionSubject) {
		super(studentClassroomSessionDivisionSubject);
		for(StudentClassroomSessionDivisionSubjectEvaluation studentSubjectEvaluation : studentClassroomSessionDivisionSubject.getDetails()){
			if(studentSubjectEvaluation.getStudentClassroomSessionDivisionSubject().equals(studentClassroomSessionDivisionSubject)){
				if(studentSubjectEvaluation.getEvaluation().getClassroomSessionDivisionSubjectEvaluationType().getEvaluationType().getCode().equals(SchoolConstant.Code.EvaluationType.TEST1))
					test1 = numberBusiness.format(studentSubjectEvaluation.getValue());
				else if(studentSubjectEvaluation.getEvaluation().getClassroomSessionDivisionSubjectEvaluationType().getEvaluationType().getCode().equals(SchoolConstant.Code.EvaluationType.TEST2))
					test2 = numberBusiness.format(studentSubjectEvaluation.getValue());
				else if(studentSubjectEvaluation.getEvaluation().getClassroomSessionDivisionSubjectEvaluationType().getEvaluationType().getCode().equals(SchoolConstant.Code.EvaluationType.EXAM))
					exam = numberBusiness.format(studentSubjectEvaluation.getValue());
			}	
		}
	}
	
	public static final String FIELD_TEST1 = "test1";
	public static final String FIELD_TEST2 = "test2";
	public static final String FIELD_EXAM = "exam";

}