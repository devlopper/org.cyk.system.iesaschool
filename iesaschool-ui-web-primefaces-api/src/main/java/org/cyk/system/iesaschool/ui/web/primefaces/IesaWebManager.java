package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter.SecuredUrlProvider;
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
		
		
		WebEnvironmentAdapter.SECURED_URL_PROVIDERS.add(new SecuredUrlProvider() {
			
			@Override
			public void provide() {
				
			}
		});
	}
		
	public static IesaWebManager getInstance() {
		return INSTANCE;
	}
	
	@Override
	public SystemMenu systemMenu(AbstractUserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		
		systemMenu.getBusinesses().add(menuManager.crudMany(Teacher.class, null));
		systemMenu.getBusinesses().add(menuManager.crudMany(Student.class, null));
		systemMenu.getBusinesses().add(menuManager.crudMany(ClassroomSession.class, null));
		
		return systemMenu;
	}
	
	
	
}
