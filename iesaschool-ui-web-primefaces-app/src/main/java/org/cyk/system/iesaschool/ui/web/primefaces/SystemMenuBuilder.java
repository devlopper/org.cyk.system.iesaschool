package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

public class SystemMenuBuilder extends org.cyk.system.school.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}

	
}
