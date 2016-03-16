package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.model.party.person.JobTitle;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.school.business.api.session.SchoolReportProducer;
import org.cyk.system.school.business.api.session.StudentClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubjectEvaluationType;
import org.cyk.system.school.model.subject.StudentSubject;
import org.cyk.system.school.ui.web.primefaces.session.StudentClassroomSessionDivisionConsultPage;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.UICommandable.IconType;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

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
    	StudentClassroomSessionDivisionBusiness.BuildReportArguments.ATTENDANCE = Boolean.FALSE;
		
	}
		
	public static IesaWebManager getInstance() {
		return INSTANCE;
	}
	
	@Override
	public SystemMenu systemMenu(AbstractUserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		/*
		UICommandable group = uiProvider.createCommandable("iesa.menu.registration", null);		
		group.addChild(menuManager.crudMany(Student.class, null));
		group.addChild(menuManager.crudMany(Teacher.class, null));
		group.addChild(menuManager.crudMany(Employee.class, null));
		systemMenu.getBusinesses().add(group);
		
		group = uiProvider.createCommandable("iesa.menu.classes", null);		
		group.addChild(menuManager.crudMany(ClassroomSession.class, null));
		group.addChild(menuManager.createMany(StudentClassroomSession.class, null));
		group.addChild(menuManager.createMany(StudentSubject.class, null));
		//group.addChild(menuManager.createSelect(ClassroomSessionDivisionSubject.class,SchoolBusinessLayer.getInstance().getActionCreateSubjectEvaluation() ,null));
		//group.addChild(menuManager.createSelect(ClassroomSessionDivision.class,SchoolBusinessLayer.getInstance().getActionUpdateStudentClassroomSessionDivisionResults() ,null));
		systemMenu.getBusinesses().add(group);
		
		group = uiProvider.createCommandable("iesa.menu.configuration", null);		
		group.addChild(menuManager.crudMany(Company.class, null));
		group.addChild(menuManager.crudMany(PersonTitle.class, null));
		group.addChild(menuManager.crudMany(JobTitle.class, null));
		systemMenu.getBusinesses().add(group);
		*/
		systemMenu.getBusinesses().add(getRegistrationCommandable(userSession, null));
		systemMenu.getBusinesses().add(getClassCommandable(userSession, null));			
		systemMenu.getBusinesses().add(getMarksCardCommandable(userSession, null));
		
		systemMenu.getReferenceEntities().add(getControlPanelCommandable(userSession, null));
		
		return systemMenu;
	}
	
	public UICommandable getRegistrationCommandable(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = uiProvider.createCommandable("command.actor.registration", IconType.PERSON);
		module.addChild(menuManager.crudMany(Student.class, null));
		module.addChild(menuManager.crudMany(Teacher.class, null));
		module.addChild(menuManager.crudMany(Employee.class, null));
		return module;
	}
	
	public UICommandable getClassCommandable(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = uiProvider.createCommandable(businessEntityInfos(ClassroomSession.class).getUserInterface().getLabelId(), null);
		module.addChild(menuManager.crudMany(ClassroomSession.class, null));
		module.addChild(menuManager.createMany(StudentClassroomSession.class, null));
		module.addChild(menuManager.createMany(StudentSubject.class, null));
		module.addChild(menuManager.createSelectOne(ClassroomSessionDivisionSubjectEvaluationType.class,SchoolBusinessLayer.getInstance().getActionCreateSubjectEvaluation() ,null));
		module.addChild(menuManager.createSelectOne(ClassroomSessionDivision.class,SchoolBusinessLayer.getInstance().getActionUpdateStudentClassroomSessionDivisionResults() ,null));
		return module;
	}
	
	public UICommandable getMarksCardCommandable(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = uiProvider.createCommandable("school.markscard", null);
		module.addChild(menuManager.createSelectMany(ClassroomSession.class,SchoolBusinessLayer.getInstance().getActionUpdateStudentClassroomSessionDivisionReportFiles() ,null));
		module.addChild(menuManager.createSelectMany(ClassroomSession.class,SchoolBusinessLayer.getInstance().getActionConsultStudentClassroomSessionDivisionReportFiles() ,null));
		
		//module.addChild(uiProvider.createCommandable("school", null,outcomeConsultSchoolStudentClassroomSessionDivisionReportFile));
		//module.addChild(menuManager.createSelectOne(ClassroomSession.class,SchoolBusinessLayer.getInstance().getActionConsultStudentClassroomSessionDivisionReportFiles() ,null));
		return module;
	}
	
	public UICommandable getControlPanelCommandable(AbstractUserSession userSession,Collection<UICommandable> mobileCommandables){
		UICommandable module = uiProvider.createCommandable("commandable.school", null);
		module.addChild(menuManager.crudMany(Company.class, null));
		module.addChild(menuManager.crudMany(PersonTitle.class, null));
		module.addChild(menuManager.crudMany(JobTitle.class, null));
		return module;
	}
	
}
