package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.system.school.ui.web.primefaces.AbstractSchoolContextListener;
import org.cyk.system.school.ui.web.primefaces.SchoolWebManager;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.AbstractMenu;
import org.cyk.ui.api.command.menu.MenuManager;
import org.cyk.ui.api.command.menu.MenuManager.ModuleGroup;
import org.cyk.ui.api.command.menu.UIMenu;
import org.cyk.ui.web.primefaces.UserSession;

@WebListener
public class ContextListener extends AbstractSchoolContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		SchoolWebManager.getInstance().getListeners().add(new SchoolWebManager.Listener.Adapter(){
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
			
		});
		//registerConsultPageListener(Company.class, new CompanyConsultPageAdapter());
		//registerBusinessEntityFormOnePageListener(Company.class, new CompanyEditPageAdapter.Default());
	}
	
	
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		RootWebManager.getInstance().setAutoAddToSystemMenu(Boolean.FALSE);
		CompanyWebManager.getInstance().setAutoAddToSystemMenu(Boolean.FALSE);
		SchoolWebManager.getInstance().setAutoAddToSystemMenu(Boolean.FALSE);
		
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
		uiManager.registerApplicationUImanager(SchoolWebManager.getInstance());
		uiManager.registerApplicationUImanager(IesaWebManager.getInstance());
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
		
		commandable.addChild(Builder.create("MyCommand", null));
	}
	
	@Override
	public void sessionContextualMenuCreated(UserSession userSession,UIMenu menu) {
		super.sessionContextualMenuCreated(userSession, menu);
		menu.remove(MenuManager.COMMANDABLE_EVENT_CALENDAR_IDENTIFIER);
		menu.remove(MenuManager.COMMANDABLE_NOTIFICATIONS_IDENTIFIER);
	}
	
	/**/
	
}
