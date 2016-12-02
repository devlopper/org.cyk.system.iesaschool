package org.cyk.system.iesaschool.business.impl.integration;

public class CreateMemoryDatabaseBusinessNoDataIT extends AbstractCreateDatabaseBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
  
    @Override
    protected Boolean noData() {
    	/*for(ReportTemplate reportTemplate : inject(ReportTemplateDao.class).readAll())
    		//if(reportTemplate.getTemplate()!=null && reportTemplate.getBackgroundImage()!=null && reportTemplate.getDraftBackgroundImage()!=null)
    		System.out.println((reportTemplate.getTemplate()==null ? "":reportTemplate.getTemplate().getIdentifier())
    				+" : "+(reportTemplate.getBackgroundImage()==null ? "":reportTemplate.getBackgroundImage().getIdentifier())
    				+" : "+(reportTemplate.getDraftBackgroundImage()==null?"":reportTemplate.getDraftBackgroundImage().getIdentifier()));*/
    	return Boolean.TRUE;
    }
}
