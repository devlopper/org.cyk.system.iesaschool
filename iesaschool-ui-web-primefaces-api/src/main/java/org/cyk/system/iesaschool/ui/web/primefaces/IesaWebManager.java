package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.Arrays;

import javax.inject.Singleton;

import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.school.business.api.session.SchoolReportProducer;
import org.cyk.system.school.ui.web.primefaces.SchoolWebManager;
import org.cyk.system.school.ui.web.primefaces.session.student.StudentClassroomSessionDivisionConsultPage;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.ui.web.primefaces.UserSession;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=IesaWebManager.DEPLOYMENT_ORDER) @Getter
public class IesaWebManager extends AbstractPrimefacesManager implements Serializable {

	public static final int DEPLOYMENT_ORDER = IesaBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = 7231721191071228908L;

	private static IesaWebManager INSTANCE;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		identifier = "iesaschool";
		super.initialisation();  
		StudentClassroomSessionDivisionConsultPage.LOAD_EVALUATIONS = Boolean.TRUE;
		StudentClassroomSessionDivisionConsultPage.SUBJECT_DETAILS_CLASS_NAME = SubjectDetails.class.getName();
		SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.getEvaluationTypeCodes()
		.addAll(Arrays.asList(IesaConstant.EVALUATION_TYPE_TEST1,IesaConstant.EVALUATION_TYPE_TEST2,IesaConstant.EVALUATION_TYPE_EXAM));
    	SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.setSumMarks(Boolean.TRUE);
	}
	
	@Override
	public SystemMenu systemMenu(UserSession userSession) {
		return SchoolWebManager.getInstance().systemMenu(userSession);
	}
	
	public static IesaWebManager getInstance() {
		return INSTANCE;
	}

	
		
}
