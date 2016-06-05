package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.session.StudentClassroomSessionDetails;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.ui.web.primefaces.session.AbstractLevelTimeDivisionConsultPage;
import org.cyk.system.school.ui.web.primefaces.session.StudentClassroomSessionConsultManyRankPage;
import org.cyk.ui.web.primefaces.Table;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class LevelTimeDivisionCustomConsultPage extends AbstractLevelTimeDivisionConsultPage implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	private Table<StudentClassroomSessionDetails> customBroadsheetTable;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		StudentClassroomSessionConsultManyRankPage.TableAdapter tableAdapter = new StudentClassroomSessionConsultManyRankPage.TableAdapter() {
			private static final long serialVersionUID = -2065739751466832899L;
			@Override
			protected Collection<StudentClassroomSession> getStudentClassroomSessions() {
				return SchoolBusinessLayer.getInstance().getStudentClassroomSessionBusiness().findByCriteria(
						IesaBusinessLayer.getInstance().getStudentClassroomSessionSearchCriteria().addClassroomSessions(SchoolBusinessLayer.getInstance().getClassroomSessionBusiness().findByLevelTimeDivision(identifiable)));
			}
		};
		
		tableAdapter.setTitleId("iesaschool.menu.customranks");
		tableAdapter.setTabId("iesaschool.menu.customranks");
		customBroadsheetTable = (Table<StudentClassroomSessionDetails>) createDetailsTable(StudentClassroomSessionDetails.class, tableAdapter);
		
		List<ClassroomSessionDivision> classroomSessionDivisions = new ArrayList<>(StudentClassroomSessionConsultManyRankPage.filterClassroomSessionDivisions(SchoolBusinessLayer.getInstance().getClassroomSessionDivisionBusiness().findByLevelTimeDivision(identifiable)));
			
		customBroadsheetTable.getColumnListeners().add(new StudentClassroomSessionConsultManyRankPage.ColumnAdapter(userSession, classroomSessionDivisions));
		
		customBroadsheetTable.getCellListeners().add(new StudentClassroomSessionConsultManyRankPage.CellAdpater(classroomSessionDivisions) {
			private static final long serialVersionUID = -4986730778696471541L;
			@Override
			protected Collection<StudentClassroomSessionDivision> getDetailCollection() {
				return SchoolBusinessLayer.getInstance().getStudentClassroomSessionDivisionBusiness().findByLevelTimeDivision(identifiable);
			}
		});
	
	}
}