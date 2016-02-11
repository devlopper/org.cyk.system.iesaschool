package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.root.business.impl.AbstractBusinessTestHelper;

@Singleton
public class IesaBusinessTestHelper extends AbstractBusinessTestHelper implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	private static IesaBusinessTestHelper INSTANCE;
	
	/**/
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
	}

	public static IesaBusinessTestHelper getInstance() {
		return INSTANCE;
	}

}
