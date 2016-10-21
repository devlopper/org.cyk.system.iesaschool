package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.root.model.geography.Country;
import org.cyk.system.root.model.language.Language;
import org.cyk.system.root.model.party.person.Allergy;
import org.cyk.system.root.model.party.person.JobFunction;
import org.cyk.system.root.model.party.person.JobTitle;
import org.cyk.system.root.model.party.person.Medication;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.party.person.PersonRelationshipType;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.root.model.security.Role;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.UserSession;

public class SystemMenuBuilder extends org.cyk.system.school.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		if(userSession.hasRole(Role.MANAGER)){
			addBusinessMenu(userSession,systemMenu,getStudentCommandable(userSession, null));
			addBusinessMenu(userSession,systemMenu,getEmployeeCommandable(userSession, null));
		}
		//addBusinessMenu(userSession,systemMenu,getFinanceCommandable(userSession, null));
		//addBusinessMenu(userSession,systemMenu,getAcademicCommandable(userSession, null));
		//addBusinessMenu(userSession,systemMenu,getServiceCommandable(userSession, null));
		//addBusinessMenu(userSession,systemMenu,getMessageCommandable(userSession, null));
		
		addBusinessMenu(userSession,systemMenu,getRegularActivitiesCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getResultsCardCommandable(userSession, null));
		
		if(userSession.hasRole(Role.MANAGER)){
		UICommandable module = createModuleCommandable("", null);
			module.setLabel("Référence");
			module.addChild(createListCommandable(Person.class, null));
			module.addChild(createListCommandable(PersonTitle.class, null));
			module.addChild(createListCommandable(JobFunction.class, null));
			module.addChild(createListCommandable(JobTitle.class, null));
			module.addChild(createListCommandable(PersonRelationshipType.class, null));
			
			module.addChild(createListCommandable(Country.class, null));
			
			module.addChild(createListCommandable(Language.class, null));
			
			module.addChild(createListCommandable(Medication.class, null));
			module.addChild(createListCommandable(Allergy.class, null));
			
			module.addChild(createListCommandable(AcademicSession.class, null));
			module.addChild(createListCommandable(Subject.class, null));
			
			systemMenu.getBusinesses().add(module);
		}
		initialiseNavigatorTree(userSession);//TODO make it as a call after .build
		return systemMenu;
	}
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}

	
}
