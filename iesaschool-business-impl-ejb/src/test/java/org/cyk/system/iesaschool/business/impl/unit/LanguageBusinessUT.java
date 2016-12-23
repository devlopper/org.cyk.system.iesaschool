package org.cyk.system.iesaschool.business.impl.unit;

import java.util.Collection;

import org.cyk.system.root.business.impl.language.LanguageBusinessImpl;
import org.cyk.utility.test.unit.AbstractUnitTest;
import org.mockito.InjectMocks;

public class LanguageBusinessUT extends AbstractUnitTest {

	private static final long serialVersionUID = 124355073578123984L;

	@InjectMocks private LanguageBusinessImpl languageBusiness;
	
	@Override
	protected void registerBeans(Collection<Object> collection) {
		super.registerBeans(collection);
		collection.add(languageBusiness);
	}

	
	
}
