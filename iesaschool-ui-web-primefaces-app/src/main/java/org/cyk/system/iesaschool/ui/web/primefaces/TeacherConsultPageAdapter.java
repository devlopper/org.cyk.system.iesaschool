package org.cyk.system.iesaschool.ui.web.primefaces;

import java.lang.reflect.Field;

import org.cyk.system.school.model.actor.Teacher;
import org.cyk.ui.api.model.party.DefaultPersonEditFormModel;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.cyk.ui.web.primefaces.page.crud.AbstractActorConsultPage;
import org.cyk.ui.web.primefaces.page.crud.AbstractActorConsultPage.MainDetails;
import org.cyk.utility.common.cdi.AbstractBean;

public class TeacherConsultPageAdapter extends AbstractActorConsultPage.Adapter<Teacher>{

	private static final long serialVersionUID = -5657492205127185872L;

	public TeacherConsultPageAdapter() {
		super(Teacher.class);
	}
	
	@Override
	public void initialisationEnded(AbstractBean bean) {
		super.initialisationEnded(bean);
		((AbstractActorConsultPage<?>)bean).removeDetailsMenuCommandable(DefaultPersonEditFormModel.TAB_CONTACT_ID);
		((AbstractActorConsultPage<?>)bean).removeDetailsMenuCommandable(DefaultPersonEditFormModel.TAB_JOB_ID);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <DETAILS> ControlSetAdapter<DETAILS> getControlSetAdapter(Class<DETAILS> detailsClass) {
		if(MainDetails.class.equals(detailsClass)){
			return (ControlSetAdapter<DETAILS>) new ControlSetAdapter<MainDetails>(){
				@Override
				public Boolean build(Field field) {
					return field.getName().equals(MainDetails.FIELD_TITLE) || field.getName().equals(MainDetails.FIELD_FIRSTNAME) 
							|| field.getName().equals(MainDetails.FIELD_LASTNAMES) || field.getName().equals(MainDetails.FIELD_REGISTRATION_CODE);
				}
			};
		}
		return super.getControlSetAdapter(detailsClass);
	}
	
}