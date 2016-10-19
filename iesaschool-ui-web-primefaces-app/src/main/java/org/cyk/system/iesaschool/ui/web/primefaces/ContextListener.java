package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.business.impl.structure.EmployeeDetails;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.EmployeeBusinessAdapter;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments;
import org.cyk.system.root.business.api.party.person.PersonBusiness;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.geography.ContactCollectionDetails;
import org.cyk.system.root.business.impl.party.person.JobDetails;
import org.cyk.system.root.business.impl.party.person.MedicalDetails;
import org.cyk.system.root.business.impl.party.person.MedicalInformationsAllergyDetails;
import org.cyk.system.root.business.impl.party.person.MedicalInformationsMedicationDetails;
import org.cyk.system.root.business.impl.party.person.PersonDetails;
import org.cyk.system.root.business.impl.party.person.PersonRelationshipDetails;
import org.cyk.system.root.business.impl.party.person.SignatureDetails;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.FileIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.school.business.api.session.AcademicSessionBusiness;
import org.cyk.system.school.business.impl.actor.StudentBusinessImpl;
import org.cyk.system.school.business.impl.actor.StudentDetails;
import org.cyk.system.school.business.impl.actor.TeacherBusinessImpl;
import org.cyk.system.school.business.impl.actor.TeacherDetails;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.persistence.api.actor.StudentDao;
import org.cyk.system.school.ui.web.primefaces.AbstractSchoolContextListener;
import org.cyk.system.school.ui.web.primefaces.SchoolWebManager;
import org.cyk.system.school.ui.web.primefaces.adapter.enterpriseresourceplanning.StudentBusinessAdapter;
import org.cyk.system.school.ui.web.primefaces.adapter.enterpriseresourceplanning.TeacherBusinessAdapter;
import org.cyk.system.school.ui.web.primefaces.session.student.StudentClassroomSessionDivisionConsultPage;
import org.cyk.ui.api.AbstractWindow;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.AbstractMenu;
import org.cyk.ui.api.command.menu.MenuManager;
import org.cyk.ui.api.command.menu.MenuManager.ModuleGroup;
import org.cyk.ui.api.command.menu.UIMenu;
import org.cyk.ui.web.primefaces.UserSession;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage.PageInstanceManager;
import org.cyk.utility.common.Constant;

@WebListener
public class ContextListener extends AbstractSchoolContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		/*SchoolWebManager.getInstance().getListeners().add(new SchoolWebManager.Listener.Adapter(){
			private static final long serialVersionUID = 2331867191141196758L;
			@Override
			public Set<String> getInvisibleCommandableIdentifiers(UserSession userSession) {
				return new HashSet<>(Arrays.asList(SchoolWebManager.COMMANDABLE_IDENTIFIER_CONSULT_STUDENTCLASSROOMSESSION_RANKS));
			}
			@Override
			public void onBusinessMenuPopulateEnded(UserSession userSession,UICommandable module) {
				super.onBusinessMenuPopulateEnded(userSession, module);
				module.addChild(Builder.create("iesaschool.menu.customranks", null,"studentClassroomSessionCustomConsultManyRankView"));
			}
			
		});*/
		
		FileIdentifiableGlobalIdentifier.define(StudentClassroomSessionDivision.class);
		StudentClassroomSessionDivisionConsultPage.SUBJECT_DETAILS_CLASS_NAME = StudentClassroomSessionDivisionSubjectDetails.class.getName();
		StudentClassroomSessionDivisionConsultPage.LOAD_EVALUATIONS = Boolean.TRUE;
	
    	SchoolWebManager.getInstance().getListeners().add(new PrimefacesManager());
    	
    	StudentBusinessImpl.Listener.COLLECTION.add(new StudentBusinessAdapter(){
    		private static final long serialVersionUID = 1L;

			@Override
			public void beforeCreate(Student student) {
				super.beforeCreate(student);
				if(StringUtils.isBlank(student.getCode())){
					NumberBusiness.FormatArguments orderNumberFormatArguments = new FormatArguments();
					orderNumberFormatArguments.setWidth(4);
					student.setCode(IesaConstant.IESA+Constant.CHARACTER_SLASH+inject(TimeBusiness.class).findYear(inject(AcademicSessionBusiness.class).findCurrent(null).getBirthDate())
							+inject(PersonBusiness.class).findInitials(student.getPerson())+inject(NumberBusiness.class).format(inject(StudentDao.class).countAll()+1,orderNumberFormatArguments)
							+Constant.CHARACTER_HYPHEN+student.getAdmissionLevelTimeDivision().getLevel().getGroup().getCode()
							);
				}
			}
    	});
    	
    	TeacherBusinessImpl.Listener.COLLECTION.add(new TeacherBusinessAdapter());
    	EmployeeBusinessImpl.Listener.COLLECTION.add(new EmployeeBusinessAdapter());
    			
		AbstractWindow.WindowInstanceManager.INSTANCE = new PageInstanceManager(){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean isShowDetails(Class<?> detailsClass,AbstractIdentifiable identifiable,AbstractWindow<?, ?, ?, ?, ?, ?> window) {
				if(identifiable instanceof Person){
					return isClassIn(detailsClass,PersonDetails.class,ContactCollectionDetails.class,JobDetails.class);
				}
				if(identifiable instanceof Employee){
					return isClassIn(detailsClass,EmployeeDetails.class,ContactCollectionDetails.class,SignatureDetails.class);
				}
				if(identifiable instanceof Student){
					return isClassIn(detailsClass,StudentDetails.class,ContactCollectionDetails.class,MedicalDetails.class,MedicalInformationsMedicationDetails.class
							,MedicalInformationsAllergyDetails.class,PersonRelationshipDetails.class);
				}
				if(identifiable instanceof Teacher){
					return isClassIn(detailsClass,TeacherDetails.class,ContactCollectionDetails.class,SignatureDetails.class);
				}
				return super.isShowDetails(detailsClass, identifiable,window);
			}
		};
	}
		
	/**/
	
	@Override
	public Boolean moduleGroupCreateable(UserSession userSession,ModuleGroup group) {
		if(Boolean.TRUE.equals(userSession.getIsAdministrator()))
			return true;
		return !(ModuleGroup.CONTROL_PANEL.equals(group) || ModuleGroup.TOOLS.equals(group));
	}
	
	@Override
	public void moduleGroupCreated(UserSession userSession,ModuleGroup group, UICommandable commandable) {
		super.moduleGroupCreated(userSession, group, commandable);
		if(ModuleGroup.USER_ACCOUNT.equals(group)){
			AbstractMenu.__remove__(MenuManager.COMMANDABLE_NOTIFICATIONS_IDENTIFIER, (List<UICommandable>) commandable.getChildren());
			AbstractMenu.__remove__(MenuManager.COMMANDABLE_USER_ACCOUNT_IDENTIFIER, (List<UICommandable>) commandable.getChildren());
		}
	}
	
	@Override
	public void applicationMenuCreated(UserSession userSession, UIMenu menu) {
		super.applicationMenuCreated(userSession, menu);
		UICommandable commandable = menu.getCommandable(SchoolWebManager.COMMANDABLE_IDENTIFIER_RESULTS);
		AbstractMenu.__remove__(SchoolWebManager.COMMANDABLE_IDENTIFIER_CONSULT_STUDENTCLASSROOMSESSION_RANKS, (List<UICommandable>) commandable.getChildren());
		
		//commandable.addChild(Builder.create("MyCommand", null));
	}
	
	@Override
	public void sessionContextualMenuCreated(UserSession userSession,UIMenu menu) {
		super.sessionContextualMenuCreated(userSession, menu);
		menu.remove(MenuManager.COMMANDABLE_EVENT_CALENDAR_IDENTIFIER);
		menu.remove(MenuManager.COMMANDABLE_NOTIFICATIONS_IDENTIFIER);
	}
	
	/**/
	
}
