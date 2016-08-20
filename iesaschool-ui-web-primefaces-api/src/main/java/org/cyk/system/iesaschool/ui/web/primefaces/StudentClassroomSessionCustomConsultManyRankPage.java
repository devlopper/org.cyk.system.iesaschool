package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.StudentClassroomSessionBusiness;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.ui.web.primefaces.session.AbstractStudentClassroomSessionConsultManyRankPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class StudentClassroomSessionCustomConsultManyRankPage extends AbstractStudentClassroomSessionConsultManyRankPage implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	@Override
	protected Collection<StudentClassroomSession> getStudentClassroomSessions() {
		return inject(StudentClassroomSessionBusiness.class).findByCriteria(
				IesaBusinessLayer.getInstance().getStudentClassroomSessionSearchCriteria().addClassroomSessions(inject(ClassroomSessionBusiness.class).findAll()));
	}
	
	
}
