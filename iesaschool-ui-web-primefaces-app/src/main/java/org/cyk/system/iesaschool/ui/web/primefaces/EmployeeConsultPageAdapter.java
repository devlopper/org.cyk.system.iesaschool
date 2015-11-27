package org.cyk.system.iesaschool.ui.web.primefaces;

import java.lang.reflect.Field;

import org.cyk.system.company.model.structure.Employee;
import org.cyk.ui.api.model.party.DefaultPersonEditFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractActorConsultPage;
import org.cyk.ui.web.primefaces.page.crud.AbstractActorConsultPage.JobDetails;
import org.cyk.ui.web.primefaces.page.crud.AbstractActorConsultPage.MainDetails;
import org.cyk.utility.common.cdi.AbstractBean;

public class EmployeeConsultPageAdapter extends AbstractActorConsultPage.Adapter<Employee>{

	private static final long serialVersionUID = -5657492205127185872L;

	public EmployeeConsultPageAdapter() {
		super(Employee.class);
	}
			
	@Override
	public void initialisationEnded(AbstractBean bean) {
		super.initialisationEnded(bean);
		((AbstractActorConsultPage<?>)bean).removeDetailsMenuCommandable(DefaultPersonEditFormModel.TAB_CONTACT_ID);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <DETAILS> ControlSetAdapter<DETAILS> getControlSetAdapter(Class<DETAILS> detailsClass) {
		if(MainDetails.class.equals(detailsClass)){
			return (ControlSetAdapter<DETAILS>) new ControlSetAdapter<MainDetails>(){
				@Override
				public Boolean build(Field field) {
					return field.getName().equals(MainDetails.FIELD_REGISTRATION_CODE) || field.getName().equals(MainDetails.FIELD_FIRSTNAME)
							|| field.getName().equals(MainDetails.FIELD_LASTNAMES);
				}
			};
		}else if(JobDetails.class.equals(detailsClass)) {
			return (ControlSetAdapter<DETAILS>) new ControlSetAdapter<JobDetails>(){
				@Override
				public Boolean build(Field field) {
					return field.getName().equals(JobDetails.FIELD_FUNCTION);
				}
			};
		}
		return super.getControlSetAdapter(detailsClass);
	}
	
}	