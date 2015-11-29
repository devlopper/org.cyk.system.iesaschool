package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.ui.web.primefaces.structure.CompanyEditPage;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.data.collector.form.FormConfiguration;
import org.cyk.ui.web.primefaces.page.BusinessEntityFormOnePageListener;

public class CompanyEditPageAdapter extends BusinessEntityFormOnePageListener.Adapter.Default<Company> implements Serializable {

	private static final long serialVersionUID = 4370361826462886031L;

	public CompanyEditPageAdapter() {
		super(Company.class);
		FormConfiguration configuration = createFormConfiguration(Crud.UPDATE, FormConfiguration.TYPE_INPUT_SET_SMALLEST);
		configuration.addFieldNames(CompanyEditPage.Form.FIELD_SIGNER);
			
	}
	
	/**/
	
	public static class Default extends CompanyEditPageAdapter implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		

	}

}
