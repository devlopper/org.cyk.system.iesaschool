package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.TypedBusiness.CreateReportFileArguments;
import org.cyk.system.root.business.api.mathematics.IntervalBusiness;
import org.cyk.system.root.business.api.mathematics.MathematicsBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments.CharacterSet;
import org.cyk.system.root.model.file.report.LabelValueCollectionReport;
import org.cyk.system.root.model.userinterface.style.FontName;
import org.cyk.system.root.model.userinterface.style.Style;
import org.cyk.system.root.persistence.api.mathematics.MetricCollectionDao;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.model.StudentResults;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivisionReportTemplateFile;
import org.cyk.system.school.persistence.api.session.ClassroomSessionDivisionDao;
import org.cyk.system.school.persistence.api.session.StudentClassroomSessionDao;

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
	
	/*private String iesa(String text){
		return getJasperStyle(text.charAt(0)+"", FIRST_LETTER_STYLE)+getJasperStyle(text.substring(1), OTHER_LETTERS_STYLE);
	}*/
	
	@Override
	public StudentClassroomSessionDivisionReportTemplateFile produceStudentClassroomSessionDivisionReport(StudentClassroomSessionDivision studentClassroomSessionDivision,
			CreateReportFileArguments<StudentClassroomSessionDivision> arguments) {
		LabelValueCollectionReport labelValueCollectionReport;
		StudentClassroomSessionDivisionReportTemplateFile r = super.produceStudentClassroomSessionDivisionReport(studentClassroomSessionDivision,arguments);
		//r.getAcademicSession().getCompany().setName(iesa("INTERNATIONAL ")+iesa("ENGLISH ")+iesa("SCHOOL OF ")+iesa("ABIDJAN"));
		
		AcademicSession as = studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getAcademicSession();
		r.getAcademicSession().setFromDateToDate(timeBusiness.findYear(as.getBirthDate())+"/"+timeBusiness.findYear(as.getDeathDate())+" ACADEMIC SESSION");
	
		r.addLabelValueCollection("PUPIL'S DETAILS",new String[][]{
				{"Formname(s)", r.getStudent().getPerson().getLastnames()}
				,{"Surname", r.getStudent().getPerson().getGlobalIdentifier().getName()}
				,{"Date of birth", r.getStudent().getPerson().getGlobalIdentifier().getExistencePeriod().getFrom()}
				,{"Place of birth", r.getStudent().getPerson().getGlobalIdentifier().getBirthLocation()}
				,{"Admission No", r.getStudent().getGlobalIdentifier().getCode()}
				,{"Class", r.getClassroomSessionDivision().getClassroomSession().getName()}
				,{"Gender", r.getStudent().getPerson().getSex()}
				});
		
		r.addLabelValueCollection("SCHOOL ATTENDANCE",new String[][]{
				{"Number of times school opened",r.getClassroomSessionDivision().getOpenedTime()}
				,{"Number of times present",r.getAttendedTime()}
				,{"Number of times absent",r.getMissedTime()}
				});
		
		FormatArguments formatArguments = new FormatArguments();
		formatArguments.setIsRank(Boolean.TRUE);
		formatArguments.setType(CharacterSet.LETTER);
		String nameFormat = numberBusiness.format(studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber(), formatArguments).toUpperCase();
		nameFormat += " TERM , %s REPORT %s";
		//r.setName(name+" TERM , "+studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel().getGroup().getName().toUpperCase()
		//		+" REPORT");
		
		String levelNameCode = studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel().getLevelName().getCode();
		if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_PK,IesaConstant.LEVEL_NAME_CODE_K1,IesaConstant.LEVEL_NAME_CODE_K2,IesaConstant.LEVEL_NAME_CODE_K3},levelNameCode)){
			r.setName(r.getName()+" SHEET");
			String performanceCodeMetricCollectionCode = null;
			if(IesaConstant.LEVEL_NAME_CODE_PK.equals(levelNameCode)){
				addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), new String[][]{
						{performanceCodeMetricCollectionCode = IesaConstant.MERIC_COLLECTION_PK_STUDENT_EXPRESSIVE_LANGUAGE,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_PK_STUDENT_RECEPTIVE_LANGUAGE,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_PK_STUDENT_READING_READNESS,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_PK_STUDENT_NUMERACY_DEVELOPMENT,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_PK_STUDENT_ARTS_MUSIC,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_PK_STUDENT_SOCIAL_EMOTIONAL_DEVELOPMENT,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_PK_STUDENT_GROSS_MOTOR_SKILLS,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_PK_STUDENT_FINE_MOTOR_SKILLS,NOT_APPLICABLE}
					});
			}else if(IesaConstant.LEVEL_NAME_CODE_K1.equals(levelNameCode)){
				addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), new String[][]{
						{performanceCodeMetricCollectionCode = IesaConstant.MERIC_COLLECTION_K1_STUDENT_ENGLISH_LANGUAGE_ARTS_READING,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K1_STUDENT_COMMUNICATION_SKILLS,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K1_STUDENT_SCIENCE,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K1_STUDENT_SOCIAL_STUDIES,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K1_STUDENT_MATHEMATICS,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K1_STUDENT_WORK_HABITS,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K1_STUDENT_SOCIAL_SKILLS,NOT_APPLICABLE}
					});
			}else if(IesaConstant.LEVEL_NAME_CODE_K2.equals(levelNameCode) || IesaConstant.LEVEL_NAME_CODE_K3.equals(levelNameCode)){
				addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), new String[][]{
						{performanceCodeMetricCollectionCode = IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_READING_READINESS,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_READING,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_WRITING,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_LISTENING_SPEAKING_VIEWING,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_ALPHABET_IDENTIFICATION,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_MATHEMATICS,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_SCIENCE_SOCIAL_STUDIES_MORAL_EDUCATION,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_ART_CRAFT,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_MUSIC,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_PHYSICAL_EDUCATION,NOT_APPLICABLE}
						, {IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_WORK_BEHAVIOUR_HABITS,NOT_APPLICABLE}
					});
			}
			
			labelValueCollectionReport = addIntervalCollectionLabelValueCollection(r,inject(MetricCollectionDao.class).read(performanceCodeMetricCollectionCode).getValueIntervalCollection()
					,Boolean.TRUE,Boolean.FALSE,null);
			labelValueCollectionReport.add("NA", "Not Assessed");
		}else{
			String studentBehaviourMetricCollectionCode = null;
			r.setName(r.getName()+" CARD");
			//r.setSubjectsBlockTitle("COGNITIVE ASSESSMENT");
			String testCoef = null,examCoef = "";	
			if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G1,IesaConstant.LEVEL_NAME_CODE_G2,IesaConstant.LEVEL_NAME_CODE_G3},levelNameCode)){
				r.setName(String.format(nameFormat, "LOWER PRIMARY","CARD"));
				testCoef = "15";
				examCoef = "70";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G1_G6_STUDENT_BEHAVIOUR;
			}else if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G4,IesaConstant.LEVEL_NAME_CODE_G5,IesaConstant.LEVEL_NAME_CODE_G6},levelNameCode)){
				r.setName(String.format(nameFormat, "UPPER PRIMARY","CARD"));
				testCoef = "15";
				examCoef = "70";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G1_G6_STUDENT_BEHAVIOUR;
			}else if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G7,IesaConstant.LEVEL_NAME_CODE_G8,IesaConstant.LEVEL_NAME_CODE_G9},levelNameCode)){
				r.setName(String.format(nameFormat, "JUNIOR HIGH SCHOOL","CARD"));
				testCoef = "20";
				examCoef = "60";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G7_G12_STUDENT_BEHAVIOUR;
			}else if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G10,IesaConstant.LEVEL_NAME_CODE_G11,IesaConstant.LEVEL_NAME_CODE_G12},levelNameCode)){
				r.setName(String.format(nameFormat, "SENIOR HIGH SCHOOL","CARD"));
				testCoef = "20";
				examCoef = "60";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G7_G12_STUDENT_BEHAVIOUR;
			}
			
			r.addSubjectsTableColumnNames("No.","SUBJECTS","TEST 1 "+testCoef+"%","TEST 2 "+testCoef+"%","EXAM "+examCoef+"%","TOTAL 100%","GRADE","RANK","OUT OF","MAX","CLASS AVERAGE","REMARKS","TEACHER");
			
			labelValueCollectionReport = new LabelValueCollectionReport();
			labelValueCollectionReport.setName("OVERALL RESULT");
			labelValueCollectionReport.add("AVERAGE",r.getAverage());
			labelValueCollectionReport.add("GRADE",r.getAverageScale());
			if(Boolean.TRUE.equals(studentClassroomSessionDivision.getClassroomSessionDivision().getStudentRankable()))
				labelValueCollectionReport.add("RANK",r.getRank());
			r.addLabelValueCollection(labelValueCollectionReport);
			
			addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), studentBehaviourMetricCollectionCode);
			r.getCurrentLabelValueCollection().setName(StringUtils.upperCase(r.getCurrentLabelValueCollection().getName()));
			labelValueCollectionReport = new LabelValueCollectionReport();
			labelValueCollectionReport.setName(r.getCurrentLabelValueCollection().getName());
			labelValueCollectionReport.setCollection(r.getCurrentLabelValueCollection().getCollection().subList(6, 12));
			r.getCurrentLabelValueCollection().setCollection(r.getCurrentLabelValueCollection().getCollection().subList(0, 6));
			
			r.addLabelValueCollection(labelValueCollectionReport);
			
			addIntervalCollectionLabelValueCollection(r,inject(ClassroomSessionBusiness.class).findCommonNodeInformations(
				((StudentClassroomSessionDivision)r.getSource()).getClassroomSessionDivision().getClassroomSession()).getStudentClassroomSessionDivisionAverageScale()
				,Boolean.FALSE,Boolean.TRUE,new Integer[][]{{1,2}});
			r.getCurrentLabelValueCollection().setName(StringUtils.upperCase(r.getCurrentLabelValueCollection().getName()));
			
			addIntervalCollectionLabelValueCollection(r,inject(MetricCollectionDao.class).read(studentBehaviourMetricCollectionCode).getValueIntervalCollection()
					,Boolean.TRUE,Boolean.FALSE,null);
			r.getCurrentLabelValueCollection().setName(StringUtils.upperCase(r.getCurrentLabelValueCollection().getName()));
			
		}
		
		if(studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber()==3){
			StudentResults classroomSessionResults = inject(StudentClassroomSessionDao.class)
					.readByStudentByClassroomSession(studentClassroomSessionDivision.getStudent(), studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession()).getResults();
			
			r.addLabelValueCollection("HOME/SCHOOL COMMUNICATIONS",new String[][]{
					{"ANNUAL AVERAGE",format(classroomSessionResults.getEvaluationSort().getAverage().getValue())}
					,{"ANNUAL GRADE"
						,classroomSessionResults.getEvaluationSort().getAverageAppreciatedInterval()==null?NULL_VALUE:inject(IntervalBusiness.class).findRelativeCode(classroomSessionResults.getEvaluationSort().getAverageAppreciatedInterval())}
					,{"ANNUAL RANK",inject(MathematicsBusiness.class).format(classroomSessionResults.getEvaluationSort().getRank())}
					,{"PROMOTION INFORMATION",
						classroomSessionResults.getEvaluationSort().getAveragePromotedInterval()==null?NULL_VALUE:classroomSessionResults.getEvaluationSort().getAveragePromotedInterval().getName().toUpperCase()}
					,{"NEXT ACADEMIC SESSION",format(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getAcademicSession().getNextStartingDate())}
							
			});
			
		}else{
			ClassroomSessionDivision nextClassroomSessionDivision = inject(ClassroomSessionDivisionDao.class)
					.readByClassroomSessionByOrderNumber(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession()
							, studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber());
		
			r.addLabelValueCollection("HOME/SCHOOL COMMUNICATIONS",new String[][]{
				{"CONFERENCE REQUESTED",studentClassroomSessionDivision.getResults().getConferenceRequested()==null?"NO"
						:studentClassroomSessionDivision.getResults().getConferenceRequested()?"YES":"NO"}
				,{"NEXT OPENING",format(nextClassroomSessionDivision.getBirthDate())}
				,{"NEXT TERM EXAMINATION",format(nextClassroomSessionDivision.getDeathDate())}
				});
		}
	
		
		
		return r;
	}
		
}