package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.ui.web.primefaces.session.AbstractStudentClassroomSessionConsultManyRankPage;

@Named @ViewScoped @Getter @Setter
public class StudentClassroomSessionCustomConsultManyRankPage extends AbstractStudentClassroomSessionConsultManyRankPage implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	@Override
	protected Collection<StudentClassroomSession> getStudentClassroomSessions() {
		StudentClassroomSession.SearchCriteria searchCriteria = new StudentClassroomSession.SearchCriteria();
		searchCriteria.getDivisionCount().setLowest(2);
		searchCriteria.getDivisionCount().setHighest(3);
		searchCriteria.getDivisionIndexesRequired().add(2);
		return SchoolBusinessLayer.getInstance().getStudentClassroomSessionBusiness().findByCriteria(searchCriteria);
	}
	
	
}
