package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;

import org.cyk.system.root.model.file.report.LabelValueCollectionReport;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivisionReport;

public class ReportProducer extends AbstractSchoolReportProducer implements Serializable{
	
	private static final long serialVersionUID = 246685915578107971L;
	
	@Override
	public StudentClassroomSessionDivisionReport produceStudentClassroomSessionDivisionReport(StudentClassroomSessionDivision studentClassroomSessionDivision,
			StudentClassroomSessionDivisionReportParameters parameters) {
		StudentClassroomSessionDivisionReport r = super.produceStudentClassroomSessionDivisionReport(studentClassroomSessionDivision,parameters);
		r.getAcademicSession().getCompany().setName("<style forecolor=\"red\">I</style>NTERNATIONAL <style forecolor=\"red\">E</style>NGLISH <style forecolor=\"red\">S</style>CHOOL"
				+ " OF <style forecolor=\"red\">A</style>BIDJAN");
		
		r.getSubjectsTableColumnNames().add("No.");
		r.getSubjectsTableColumnNames().add("SUBJECTS");
		r.getSubjectsTableColumnNames().add("Test 1 15%");
		r.getSubjectsTableColumnNames().add("Test 2 15%");
		r.getSubjectsTableColumnNames().add("Exam 70%");
		r.getSubjectsTableColumnNames().add("TOTAL 100%");
		r.getSubjectsTableColumnNames().add("GRADE");
		r.getSubjectsTableColumnNames().add("RANK");
		r.getSubjectsTableColumnNames().add("OUT OF");
		r.getSubjectsTableColumnNames().add("MAX");
		r.getSubjectsTableColumnNames().add("CLASS AVERAGE");
		r.getSubjectsTableColumnNames().add("REMARKS");
		r.getSubjectsTableColumnNames().add("TEACHER");
		
		r.setInformationLabelValueCollection(labelValueCollection("school.report.studentclassroomsessiondivision.block.informations"));
		if(SchoolBusinessLayer.getInstance().getClassroomSessionDivisionBusiness().findIndex(studentClassroomSessionDivision.getClassroomSessionDivision())==3){
			labelValue("school.report.studentclassroomsessiondivision.block.informations.annualaverage", "To Compute");
			labelValue("school.report.studentclassroomsessiondivision.block.informations.annualgrade", "To Compute");
			labelValue("school.report.studentclassroomsessiondivision.block.informations.annualrank", "To Compute");
			//labelValue("school.report.studentclassroomsessiondivision.block.informations.promotion", 
			//		studentClassroomSessionDivision.get "To Compute");
		}else{
			labelValue("school.report.studentclassroomsessiondivision.block.informations.nextacademicsession", 
					format(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getAcademicSession().getNextStartingDate()));
		}
		
		r.setBehaviorLabelValueCollection1(new LabelValueCollectionReport());
		r.getBehaviorLabelValueCollection1().setName("school.report.studentclassroomsessiondivision.block.behaviour");
		for(int i=0;i<=5;i++)
			r.getBehaviorLabelValueCollection1().getCollection().add(r.getBehaviorLabelValueCollection().getCollection().get(i));
		
		r.setBehaviorLabelValueCollection2(new LabelValueCollectionReport());
		r.getBehaviorLabelValueCollection2().setName("school.report.studentclassroomsessiondivision.block.behaviour");
		for(int i=6;i<=11;i++)
			r.getBehaviorLabelValueCollection2().getCollection().add(r.getBehaviorLabelValueCollection().getCollection().get(i));
		
		return r;
	}
	
}