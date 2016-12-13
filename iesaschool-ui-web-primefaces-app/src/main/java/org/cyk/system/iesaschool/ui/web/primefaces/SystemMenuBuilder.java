package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.UserSession;

public class SystemMenuBuilder extends org.cyk.system.school.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			SystemMenuBuilder.DEFAULT = INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}

	@Override
	public Commandable getPaymentCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables) {
		return null;
	}
	
	@Override
	public Commandable getSaleCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables) {
		return null;
	}
	
}
