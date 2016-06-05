package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.ui.web.primefaces.session.AbstractStudentClassroomSessionConsultManyRankPage;

@Named @ViewScoped @Getter @Setter
public class StudentClassroomSessionCustomConsultManyRankPage extends AbstractStudentClassroomSessionConsultManyRankPage implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	@Override
	protected Collection<StudentClassroomSession> getStudentClassroomSessions() {
		return SchoolBusinessLayer.getInstance().getStudentClassroomSessionBusiness().findByCriteria(
				IesaBusinessLayer.getInstance().getStudentClassroomSessionSearchCriteria().addClassroomSessions(SchoolBusinessLayer.getInstance().getClassroomSessionBusiness().findAll()));
	}
	
	
}
