package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.school.model.subject.StudentSubject;
import org.cyk.system.school.model.subject.StudentSubjectEvaluation;
import org.cyk.system.school.ui.web.primefaces.session.StudentClassroomSessionDivisionConsultPage.AbstractSubjectDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.Sequence;
import org.cyk.utility.common.annotation.user.interfaces.Sequence.Direction;

public class SubjectDetails extends AbstractSubjectDetails implements Serializable{
	
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText @Sequence(direction=Direction.AFTER,field=FILED_SUBJECT) private String test1;
	@Input @InputText @Sequence(direction=Direction.AFTER,field=FIELD_TEST1) private String test2;
	@Input @InputText @Sequence(direction=Direction.AFTER,field=FIELD_TEST2) private String exam;
	public SubjectDetails(StudentSubject studentSubject) {
		super(studentSubject);
		for(StudentSubjectEvaluation studentSubjectEvaluation : studentSubject.getDetails()){
			if(studentSubjectEvaluation.getStudentSubject().equals(studentSubject)){
				if(studentSubjectEvaluation.getSubjectEvaluation().getType().getType().getCode().equals(IesaConstant.EVALUATION_TYPE_TEST1))
					test1 = numberBusiness.format(studentSubjectEvaluation.getValue());
				else if(studentSubjectEvaluation.getSubjectEvaluation().getType().getType().getCode().equals(IesaConstant.EVALUATION_TYPE_TEST2))
					test2 = numberBusiness.format(studentSubjectEvaluation.getValue());
				else if(studentSubjectEvaluation.getSubjectEvaluation().getType().getType().getCode().equals(IesaConstant.EVALUATION_TYPE_EXAM))
					exam = numberBusiness.format(studentSubjectEvaluation.getValue());
			}
				
		}
	}
	
	public static final String FIELD_TEST1 = "test1";
	public static final String FIELD_TEST2 = "test2";
	public static final String FIELD_EXAM = "exam";

}