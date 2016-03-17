package org.cyk.system.iesaschool.business.impl.integration;

public class CreateMemoryDatabaseAndRandomValuesBusinessIT extends AbstractCreateDatabaseBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Override
    protected Boolean isSimulated() {
    	return Boolean.TRUE;
    }
}
