package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.root.model.file.report.LabelValueCollectionReport;
import org.cyk.system.root.model.file.report.LabelValueReport;
import org.cyk.system.root.model.userinterface.style.FontName;
import org.cyk.system.root.model.userinterface.style.Style;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivisionReport;
import org.cyk.utility.common.Constant;

public class ReportProducer extends AbstractSchoolReportProducer implements Serializable{
	
	private static final long serialVersionUID = 246685915578107971L;
	
	private static final Style FIRST_LETTER_STYLE = new Style();
	private static final Style OTHER_LETTERS_STYLE = new Style();
	static{
		FIRST_LETTER_STYLE.getText().getColor().setHexademicalCode("FF0000");
		FIRST_LETTER_STYLE.getFont().setSize(34);
		FIRST_LETTER_STYLE.getFont().setName(FontName.TIN_BIRD_HOUSE);
		
		OTHER_LETTERS_STYLE.getText().getColor().setHexademicalCode("123456");
		OTHER_LETTERS_STYLE.getFont().setSize(22);
		OTHER_LETTERS_STYLE.getFont().setName(FontName.TIN_BIRD_HOUSE);
	}
	
	private String iesa(String text){
		return getJasperStyle(text.charAt(0)+"", FIRST_LETTER_STYLE)+getJasperStyle(text.substring(1), OTHER_LETTERS_STYLE);
	}
	
	@Override
	public StudentClassroomSessionDivisionReport produceStudentClassroomSessionDivisionReport(StudentClassroomSessionDivision studentClassroomSessionDivision,
			StudentClassroomSessionDivisionReportParameters parameters) {
		StudentClassroomSessionDivisionReport r = super.produceStudentClassroomSessionDivisionReport(studentClassroomSessionDivision,parameters);
		
		r.getAcademicSession().getCompany().setName(iesa("INTERNATIONAL ")+iesa("ENGLISH ")+iesa("SCHOOL OF ")+iesa("ABIDJAN"));
		
		AcademicSession as = studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getAcademicSession();
		r.getAcademicSession().setFromDateToDate(timeBusiness.findYear(as.getPeriod().getFromDate())+"/"+timeBusiness.findYear(as.getPeriod().getToDate())+" ACADEMIC SESSION");
	
		String name = null;
		if(studentClassroomSessionDivision.getClassroomSessionDivision().getIndex()==0)
			name = "FIRST";
		else if(studentClassroomSessionDivision.getClassroomSessionDivision().getIndex()==1)
			name = "SECOND";
		else if(studentClassroomSessionDivision.getClassroomSessionDivision().getIndex()==2)
			name = "THIRD";
		r.setName(name+" "+studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel().getGroup().getName().toUpperCase()
				+" TERM REPORT CARD");
		
		
		r.getSubjectsTableColumnNames().add("No.");
		r.getSubjectsTableColumnNames().add("SUBJECTS");
		r.getSubjectsTableColumnNames().add("TEST 1 15%");
		r.getSubjectsTableColumnNames().add("TEST 2 15%");
		r.getSubjectsTableColumnNames().add("EXAM 70%");
		r.getSubjectsTableColumnNames().add("TOTAL 100%");
		r.getSubjectsTableColumnNames().add("GRADE");
		r.getSubjectsTableColumnNames().add("RANK");
		r.getSubjectsTableColumnNames().add("OUT OF");
		r.getSubjectsTableColumnNames().add("MAX");
		r.getSubjectsTableColumnNames().add("CLASS AVERAGE");
		r.getSubjectsTableColumnNames().add("REMARKS");
		r.getSubjectsTableColumnNames().add("TEACHER");
		
		r.setInformationLabelValueCollection(labelValueCollection("school.report.studentclassroomsessiondivision.block.informations"));
		if(studentClassroomSessionDivision.getClassroomSessionDivision().getIndex()==3){
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
		r.getBehaviorLabelValueCollection1().setName(languageBusiness.findText("school.report.studentclassroomsessiondivision.block.behaviour"));
		for(int i=0;i<=5;i++)
			r.getBehaviorLabelValueCollection1().getCollection().add(r.getBehaviorLabelValueCollection().getCollection().get(i));
		
		r.setBehaviorLabelValueCollection2(new LabelValueCollectionReport());
		r.getBehaviorLabelValueCollection2().setName(languageBusiness.findText("school.report.studentclassroomsessiondivision.block.behaviour"));
		for(int i=6;i<=11;i++)
			r.getBehaviorLabelValueCollection2().getCollection().add(r.getBehaviorLabelValueCollection().getCollection().get(i));
		
		return r;
	}
	
	@Override
	protected void processGradingScaleLabelValueReport(LabelValueReport labelValueReport) {
		super.processGradingScaleLabelValueReport(labelValueReport);
		labelValueReport.setLabel(StringUtils.substringAfter(labelValueReport.getLabel(), Constant.CHARACTER_SLASH.toString()));
	}
	
}