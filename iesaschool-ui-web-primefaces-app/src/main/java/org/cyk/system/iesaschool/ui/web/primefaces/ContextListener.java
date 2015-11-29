package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.person.AbstractActor;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.ui.web.primefaces.AbstractSchoolContextListener;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.AbstractMenu;
import org.cyk.ui.api.command.menu.MenuManager;
import org.cyk.ui.api.command.menu.MenuManager.ModuleGroup;
import org.cyk.ui.api.command.menu.UIMenu;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.api.model.party.DefaultActorEditFormModel;
import org.cyk.ui.api.model.party.DefaultPersonEditFormModel;
import org.cyk.ui.web.primefaces.page.BusinessEntityFormOnePageListener;
import org.cyk.ui.web.primefaces.page.tools.AbstractActorConsultPageAdapter;

@WebListener
public class ContextListener extends AbstractSchoolContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		registerConsultPageListener(Company.class, new CompanyConsultPageAdapter());
		registerBusinessEntityFormOnePageListener(Company.class, new CompanyEditPageAdapter.Default());
	}
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(IesaWebManager.getInstance());
	}
	
	/**/
	
	@Override
	public Boolean moduleGroupCreateable(AbstractUserSession userSession,ModuleGroup group) {
		return !(ModuleGroup.CONTROL_PANEL.equals(group) || ModuleGroup.TOOLS.equals(group));
	}
	
	@Override
	public void moduleGroupCreated(AbstractUserSession userSession,ModuleGroup group, UICommandable commandable) {
		super.moduleGroupCreated(userSession, group, commandable);
		if(ModuleGroup.USER_ACCOUNT.equals(group)){
			AbstractMenu.__remove__(MenuManager.COMMANDABLE_NOTIFICATIONS_IDENTIFIER, (List<UICommandable>) commandable.getChildren());
			AbstractMenu.__remove__(MenuManager.COMMANDABLE_USER_ACCOUNT_IDENTIFIER, (List<UICommandable>) commandable.getChildren());
		}
	}
	
	@Override
	public void sessionContextualMenuCreated(AbstractUserSession userSession,UIMenu menu) {
		super.sessionContextualMenuCreated(userSession, menu);
		menu.remove(MenuManager.COMMANDABLE_EVENT_CALENDAR_IDENTIFIER);
		menu.remove(MenuManager.COMMANDABLE_NOTIFICATIONS_IDENTIFIER);
	}
	
	@Override
	protected <IDENTIFIABLE extends AbstractIdentifiable> void registerBusinessEntityFormOnePageListener(Class<IDENTIFIABLE> aClass,BusinessEntityFormOnePageListener<?> listener) {
		super.registerBusinessEntityFormOnePageListener(aClass, listener);
		if(aClass.equals(Teacher.class)){
			listener.getFormConfigurationMap().get(Crud.CREATE).get(FormConfiguration.TYPE_INPUT_SET_SMALLEST).addRequiredFieldNames(DefaultActorEditFormModel.FIELD_REGISTRATION_CODE);
			listener.getFormConfigurationMap().get(Crud.CREATE).get(FormConfiguration.TYPE_INPUT_SET_SMALLEST).addFieldNames(DefaultPersonEditFormModel.FIELD_TITLE
					,DefaultPersonEditFormModel.FIELD_SIGNATURE_SPECIMEN);
			
			listener.getFormConfigurationMap().get(Crud.UPDATE).get(DefaultPersonEditFormModel.TAB_PERSON_ID).addFieldNames(
					DefaultActorEditFormModel.FIELD_REGISTRATION_CODE,DefaultPersonEditFormModel.FIELD_TITLE);
			
		}else if(aClass.equals(Student.class)){
			listener.getFormConfigurationMap().get(Crud.CREATE).get(FormConfiguration.TYPE_INPUT_SET_SMALLEST).addRequiredFieldNames(DefaultActorEditFormModel.FIELD_REGISTRATION_CODE);
			listener.getFormConfigurationMap().get(Crud.CREATE).get(FormConfiguration.TYPE_INPUT_SET_SMALLEST).addFieldNames(DefaultPersonEditFormModel.FIELD_SURNAME
					,DefaultPersonEditFormModel.FIELD_BIRTH_DATE,DefaultPersonEditFormModel.FIELD_BIRTH_LOCATION
					,DefaultPersonEditFormModel.FIELD_SEX,DefaultPersonEditFormModel.FIELD_IMAGE);
			
			listener.getFormConfigurationMap().get(Crud.UPDATE).get(DefaultPersonEditFormModel.TAB_PERSON_ID).addFieldNames(
					DefaultActorEditFormModel.FIELD_REGISTRATION_CODE,DefaultPersonEditFormModel.FIELD_SURNAME
					,DefaultPersonEditFormModel.FIELD_BIRTH_DATE,DefaultPersonEditFormModel.FIELD_BIRTH_LOCATION
					,DefaultPersonEditFormModel.FIELD_SEX,DefaultPersonEditFormModel.FIELD_IMAGE);
		}else if(aClass.equals(Employee.class)){
			listener.getFormConfigurationMap().get(Crud.CREATE).get(FormConfiguration.TYPE_INPUT_SET_SMALLEST).addRequiredFieldNames(DefaultActorEditFormModel.FIELD_REGISTRATION_CODE);
			listener.getFormConfigurationMap().get(Crud.CREATE).get(FormConfiguration.TYPE_INPUT_SET_SMALLEST).addFieldNames(DefaultPersonEditFormModel.FIELD_NAME
					,DefaultPersonEditFormModel.FIELD_LAST_NAME,DefaultPersonEditFormModel.FIELD_TITLE,DefaultPersonEditFormModel.FIELD_SIGNATURE_SPECIMEN
					,DefaultPersonEditFormModel.FIELD_JOB_TITLE);
			
			listener.getFormConfigurationMap().get(Crud.UPDATE).get(DefaultPersonEditFormModel.TAB_PERSON_ID).addFieldNames(
					DefaultActorEditFormModel.FIELD_REGISTRATION_CODE,DefaultPersonEditFormModel.FIELD_NAME,DefaultPersonEditFormModel.FIELD_LAST_NAME);
					
		}else if(aClass.equals(Company.class)){
			
					
		}
	}
	
	/**/
	
	@SuppressWarnings("unchecked")
	@Override
	protected <ACTOR extends AbstractActor> AbstractActorConsultPageAdapter<ACTOR> getActorConsultPageAdapter(Class<ACTOR> actorClass) {
		if(actorClass.equals(Student.class))
			return (AbstractActorConsultPageAdapter<ACTOR>) new StudentConsultPageAdapter();
		else if(actorClass.equals(Teacher.class))
			return (AbstractActorConsultPageAdapter<ACTOR>) new TeacherConsultPageAdapter();
		else if(actorClass.equals(Employee.class))
			return (AbstractActorConsultPageAdapter<ACTOR>) new EmployeeConsultPageAdapter();

		return super.getActorConsultPageAdapter(actorClass);
	}
	
}
