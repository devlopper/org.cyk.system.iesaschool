package org.cyk.system.iesaschool.business.impl.integration;

import org.apache.commons.lang3.StringUtils;

public class ApplicationSetupBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    
    @Override
    protected void businesses() {
    	String[][] nodes = new String[][]{
			new String[]{"container","configuration","property",null}
		};
		readXmlNode("arquillian.xml", ARQUILLIAN_NAMESPACE, nodes);	
    	/*
		if(Boolean.TRUE.equals(StringUtils.contains(nodes[0][3], "memory.xml"))){
			
		}else if(Boolean.TRUE.equals(StringUtils.contains(nodes[0][3], "live.xml"))){
			System.out.println("Deleting");
			rootDataProducerHelper.dropDatabase();
			rootDataProducerHelper.createDatabase();
		}
		
		new Transaction(this,userTransaction,null){
			@Override
			public void _execute_() {
				getEntityManager().flush();
			}
    	}.run();
		
		*/
		
		installApplication();
		
		//getEntityManager().flush();
		
		if(Boolean.TRUE.equals(StringUtils.contains(nodes[0][3], "memory.xml"))){
			
		}else if(Boolean.TRUE.equals(StringUtils.contains(nodes[0][3], "live.xml"))){
			rootDataProducerHelper.exportDatabase();
		}
    	System.exit(0);
    }

}
