package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

public class SystemMenuBuilder extends org.cyk.system.school.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	/*
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
		
		addReferences(userSession, systemMenu, null);
		
		initialiseNavigatorTree(userSession);//TODO make it as a call after .build
		return systemMenu;
	}*/
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}

	
}
