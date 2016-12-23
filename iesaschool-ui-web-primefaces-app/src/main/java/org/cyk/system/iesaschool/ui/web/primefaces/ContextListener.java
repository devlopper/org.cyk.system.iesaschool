package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.business.impl.structure.EmployeeDetails;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.language.LanguageBusiness.FindTextResult;
import org.cyk.system.root.business.api.message.MailBusiness;
import org.cyk.system.root.business.impl.geography.ContactCollectionDetails;
import org.cyk.system.root.business.impl.language.LanguageBusinessImpl;
import org.cyk.system.root.business.impl.party.person.AbstractActorDetails;
import org.cyk.system.root.business.impl.party.person.AbstractPersonDetails;
import org.cyk.system.root.business.impl.party.person.JobDetails;
import org.cyk.system.root.business.impl.party.person.MedicalDetails;
import org.cyk.system.root.business.impl.party.person.MedicalInformationsAllergyDetails;
import org.cyk.system.root.business.impl.party.person.MedicalInformationsMedicationDetails;
import org.cyk.system.root.business.impl.party.person.PersonDetails;
import org.cyk.system.root.business.impl.party.person.PersonRelationshipDetails;
import org.cyk.system.root.business.impl.party.person.SignatureDetails;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.language.LanguageEntry;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.school.business.impl.actor.StudentDetails;
import org.cyk.system.school.business.impl.actor.TeacherDetails;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.ui.web.primefaces.AbstractSchoolContextListener;
import org.cyk.system.school.ui.web.primefaces.SchoolWebManager;
import org.cyk.system.school.ui.web.primefaces.page.StudentEditPage;
import org.cyk.system.school.ui.web.primefaces.session.student.StudentClassroomSessionDivisionConsultPage;
import org.cyk.ui.api.AbstractWindow;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.AbstractMenu;
import org.cyk.ui.api.command.menu.MenuManager;
import org.cyk.ui.api.command.menu.MenuManager.ModuleGroup;
import org.cyk.ui.api.command.menu.UIMenu;
import org.cyk.ui.api.model.party.AbstractActorEditFormModel;
import org.cyk.ui.api.model.party.AbstractPersonEditFormModel;
import org.cyk.ui.web.primefaces.UserSession;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage.PageInstanceManager;
import org.cyk.utility.common.helper.StringHelper.CaseType;

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
		
		StudentClassroomSessionDivisionConsultPage.SUBJECT_DETAILS_CLASS_NAME = StudentClassroomSessionDivisionSubjectDetails.class.getName();
		StudentClassroomSessionDivisionConsultPage.LOAD_EVALUATIONS = Boolean.TRUE;
	
    	SchoolWebManager.getInstance().getListeners().add(new PrimefacesManager());
    	 		
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
		
		LanguageBusinessImpl.cache(Locale.FRENCH, LanguageEntry.YES, null, CaseType.FURL, "Yes");//TODO not working on file why ???
		LanguageBusinessImpl.cache(Locale.FRENCH, LanguageEntry.NO, null, CaseType.FURL, "No");
		inject(MailBusiness.class).setProperties("smtp.iesaci.com", 25, "results@iesaci.com", "school2009");//TODO should not be declare here but from database
		
		LanguageBusinessImpl.Listener.COLLECTION.add(new LanguageBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning(){
			private static final long serialVersionUID = 1L;

			@Override
			public FindTextResult afterFindFieldLabelText(Class<?> aClass,Field field, FindTextResult findTextResult) {
				if(AbstractPersonEditFormModel.class.isAssignableFrom(aClass) || AbstractPersonDetails.class.isAssignableFrom(aClass)){
					if(AbstractPersonEditFormModel.FIELD_NAME.equals(field.getName()))
						findTextResult.setValue("Surname");
					else if(AbstractPersonEditFormModel.FIELD_IMAGE.equals(field.getName()))
						findTextResult.setValue("Photo");
				}
				if(AbstractActorEditFormModel.class.isAssignableFrom(aClass) || AbstractActorDetails.class.isAssignableFrom(aClass)){
					if(AbstractActorEditFormModel.FIELD_CODE.equals(field.getName()))
						findTextResult.setValue("Admission No");
					else if(AbstractActorEditFormModel.FIELD_REGISTRATION_DATE.equals(field.getName()))
						findTextResult.setValue("Date of admission");
				}
				if(StudentEditPage.Form.class.isAssignableFrom(aClass) || StudentDetails.class.isAssignableFrom(aClass)){
					if(StudentEditPage.Form.FIELD_ADMISSION_LEVEL_TIME_DIVISION.equals(field.getName()))
						findTextResult.setValue("Admission level");
				}
				return super.afterFindFieldLabelText(aClass, field, findTextResult);
			}
			
			/*@Override
			public FindTextResult afterFindFieldLabelText(Object object,Field field, FindTextResult findTextResult) {
				if(object instanceof AbstractPersonEditFormModel<?>){
					if(AbstractPersonEditFormModel.FIELD_NAME.equals(field.getName()))
						findTextResult.setValue("Surname");
					else if(AbstractPersonEditFormModel.FIELD_IMAGE.equals(field.getName()))
						findTextResult.setValue("Photo");
				}
				if(object instanceof AbstractActorEditFormModel<?>){
					if(AbstractActorEditFormModel.FIELD_CODE.equals(field.getName()))
						findTextResult.setValue("Admission No");
					else if(AbstractActorEditFormModel.FIELD_REGISTRATION_DATE.equals(field.getName()))
						findTextResult.setValue("Date of admission");
				}
				if(object instanceof StudentEditPage.Form){
					if(StudentEditPage.Form.FIELD_ADMISSION_LEVEL_TIME_DIVISION.equals(field.getName()))
						findTextResult.setValue("Admission level");
				}
				return super.afterFindFieldLabelText(object, field, findTextResult);
			}*/
		});
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
