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
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.UserSession;

public class SystemMenuBuilder extends org.cyk.system.school.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		addBusinessMenu(userSession,systemMenu,getStudentCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getEmployeeCommandable(userSession, null));
		//addBusinessMenu(userSession,systemMenu,getFinanceCommandable(userSession, null));
		//addBusinessMenu(userSession,systemMenu,getAcademicCommandable(userSession, null));
		//addBusinessMenu(userSession,systemMenu,getServiceCommandable(userSession, null));
		//addBusinessMenu(userSession,systemMenu,getMessageCommandable(userSession, null));
		
		addBusinessMenu(userSession,systemMenu,getRegularActivitiesCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getResultsCardCommandable(userSession, null));
		
		systemMenu.getReferenceEntities().add(createListCommandable(Person.class, null));
		systemMenu.getReferenceEntities().add(createListCommandable(PersonTitle.class, null));
		systemMenu.getReferenceEntities().add(createListCommandable(JobFunction.class, null));
		systemMenu.getReferenceEntities().add(createListCommandable(JobTitle.class, null));
		systemMenu.getReferenceEntities().add(createListCommandable(PersonRelationshipType.class, null));
		
		systemMenu.getReferenceEntities().add(createListCommandable(Country.class, null));
		
		systemMenu.getReferenceEntities().add(createListCommandable(Language.class, null));
		
		systemMenu.getReferenceEntities().add(createListCommandable(Medication.class, null));
		systemMenu.getReferenceEntities().add(createListCommandable(Allergy.class, null));
		
		systemMenu.getReferenceEntities().add(createListCommandable(AcademicSession.class, null));
		systemMenu.getReferenceEntities().add(createListCommandable(Subject.class, null));
		
		
		initialiseNavigatorTree(userSession);//TODO make it as a call after .build
		return systemMenu;
	}
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}

	
}
