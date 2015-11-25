package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

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
import org.cyk.ui.api.model.party.DefaultPersonEditFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractActorConsultPage;
import org.cyk.ui.web.primefaces.page.crud.AbstractActorConsultPage.MainDetails;
import org.cyk.ui.web.primefaces.page.tools.AbstractActorConsultPageAdapter;
import org.cyk.utility.common.cdi.AbstractBean;

@WebListener
public class ContextListener extends AbstractSchoolContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
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
	
	/**/
	
	@SuppressWarnings("unchecked")
	@Override
	protected <ACTOR extends AbstractActor> AbstractActorConsultPageAdapter<ACTOR> getActorConsultPageAdapter(Class<ACTOR> actorClass) {
		if(actorClass.equals(Student.class))
			return (AbstractActorConsultPageAdapter<ACTOR>) new StudentConsultPageAdapter();
		else if(actorClass.equals(Teacher.class))
			return (AbstractActorConsultPageAdapter<ACTOR>) new TeacherConsultPageAdapter();
		return super.getActorConsultPageAdapter(actorClass);
	}
	
	private static class TeacherConsultPageAdapter extends AbstractActorConsultPage.Adapter<Teacher>{

		private static final long serialVersionUID = -5657492205127185872L;

		public TeacherConsultPageAdapter() {
			super(Teacher.class);
		}
		
		@Override
		public void initialisationEnded(AbstractBean bean) {
			super.initialisationEnded(bean);
			((AbstractActorConsultPage<?>)bean).removeDetailsMenuCommandable(DefaultPersonEditFormModel.TAB_SIGNATURE_ID);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <DETAILS> ControlSetAdapter<DETAILS> getControlSetAdapter(Class<DETAILS> detailsClass) {
			if(MainDetails.class.equals(detailsClass)){
				return (ControlSetAdapter<DETAILS>) new ControlSetAdapter<MainDetails>(){
					@Override
					public Boolean build(Field field) {
						return field.getName().equals(MainDetails.FIELD_TITLE) || field.getName().equals(MainDetails.FIELD_FIRSTNAME) 
								|| field.getName().equals(MainDetails.FIELD_LASTNAMES) || field.getName().equals(MainDetails.FIELD_REGISTRATION_CODE)
								|| field.getName().equals(MainDetails.FIELD_REGISTRATION_DATE);
					}
				};
			}
			return super.getControlSetAdapter(detailsClass);
		}
		
	}
	
	private static class StudentConsultPageAdapter extends AbstractActorConsultPage.Adapter<Student>{

		private static final long serialVersionUID = -5657492205127185872L;

		public StudentConsultPageAdapter() {
			super(Student.class);
		}
				
		@Override
		public void initialisationEnded(AbstractBean bean) {
			super.initialisationEnded(bean);
			((AbstractActorConsultPage<?>)bean).removeDetailsMenuCommandable(DefaultPersonEditFormModel.TAB_CONTACT_ID);
			((AbstractActorConsultPage<?>)bean).removeDetailsMenuCommandable(DefaultPersonEditFormModel.TAB_SIGNATURE_ID);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <DETAILS> ControlSetAdapter<DETAILS> getControlSetAdapter(Class<DETAILS> detailsClass) {
			if(MainDetails.class.equals(detailsClass)){
				return (ControlSetAdapter<DETAILS>) new ControlSetAdapter<MainDetails>(){
					@Override
					public Boolean build(Field field) {
						return field.getName().equals(MainDetails.FIELD_BIRTHDATE) || field.getName().equals(MainDetails.FIELD_FIRSTNAME) 
								|| field.getName().equals(MainDetails.FIELD_LASTNAMES) || field.getName().equals(MainDetails.FIELD_REGISTRATION_CODE)
								|| field.getName().equals(MainDetails.FIELD_REGISTRATION_DATE) || field.getName().equals(MainDetails.FIELD_BIRTHLOCATION)
								|| field.getName().equals(MainDetails.FIELD_SEX) || field.getName().equals(MainDetails.FIELD_SURNAME);
					}
				};
			}
			return super.getControlSetAdapter(detailsClass);
		}
		
	}	
	
	/**/
	
}
