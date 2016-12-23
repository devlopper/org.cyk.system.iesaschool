package org.cyk.system.iesaschool.business.impl.integration;

import org.cyk.system.root.business.api.language.LanguageBusiness;
import org.junit.Test;

public class LanguageBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void populate() {}
    
    @Test
	public void findYes() {
	    assertEquals("Yes",inject(LanguageBusiness.class).findResponseText(Boolean.TRUE));
	}
	
	@Test
	public void findNo() {
	    assertEquals("No",inject(LanguageBusiness.class).findResponseText(Boolean.FALSE));
	}
        
}
