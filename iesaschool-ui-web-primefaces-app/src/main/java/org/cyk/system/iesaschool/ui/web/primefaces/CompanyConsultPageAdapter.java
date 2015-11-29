package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.cyk.system.company.business.impl.structure.CompanyDetails;
import org.cyk.system.company.ui.web.primefaces.structure.AbstractCompanyConsultPage;
import org.cyk.system.company.ui.web.primefaces.structure.CompanyConsultPage;
import org.cyk.system.company.ui.web.primefaces.structure.CompanyEditPage;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.utility.common.cdi.AbstractBean;

public class CompanyConsultPageAdapter extends CompanyConsultPage.Adapter implements Serializable{

	private static final long serialVersionUID = -5657492205127185872L;
			
	@Override
	public void initialisationEnded(AbstractBean bean) {
		super.initialisationEnded(bean);
		((AbstractCompanyConsultPage)bean).removeDetailsMenuCommandable(CompanyEditPage.Form.TAB_CONTACT_ID);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <DETAILS> ControlSetAdapter<DETAILS> getControlSetAdapter(Class<DETAILS> detailsClass) {
		if(CompanyDetails.class.equals(detailsClass)){
			return (ControlSetAdapter<DETAILS>) new ControlSetAdapter<CompanyDetails>(){
				@Override
				public Boolean build(Field field) {
					return field.getName().equals(CompanyDetails.FIELD_SIGNER);
				}
			};
		}
		return super.getControlSetAdapter(detailsClass);
	}
	
}	