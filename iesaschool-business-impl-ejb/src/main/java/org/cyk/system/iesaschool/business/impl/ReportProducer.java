package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.root.business.api.TypedBusiness.CreateReportFileArguments;
import org.cyk.system.root.business.api.mathematics.IntervalBusiness;
import org.cyk.system.root.business.api.mathematics.MathematicsBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments.CharacterSet;
import org.cyk.system.root.model.file.report.LabelValueCollectionReport;
import org.cyk.system.root.persistence.api.mathematics.IntervalCollectionDao;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.StudentResults;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivisionReportTemplateFile;
import org.cyk.system.school.persistence.api.session.ClassroomSessionDivisionDao;
import org.cyk.system.school.persistence.api.session.StudentClassroomSessionDao;

public class ReportProducer extends AbstractSchoolReportProducer.Default implements Serializable{
	
	private static final long serialVersionUID = 246685915578107971L;
	
	/*
	@Override
	public StudentClassroomSessionDivisionReportTemplateFile produceStudentClassroomSessionDivisionReport(StudentClassroomSessionDivision studentClassroomSessionDivision,
			CreateReportFileArguments<StudentClassroomSessionDivision> arguments) {
		LabelValueCollectionReport labelValueCollectionReport;
		StudentClassroomSessionDivisionReportTemplateFile report = super.produceStudentClassroomSessionDivisionReport(studentClassroomSessionDivision,arguments);
		
		AcademicSession as = studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getAcademicSession();
		report.getAcademicSession().setFromDateToDate(timeBusiness.findYear(as.getBirthDate())+"/"+timeBusiness.findYear(as.getDeathDate())+" ACADEMIC SESSION");
	
		String levelNameCode = studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel().getLevelName().getCode();
		String effortLevelsIntervalCollectionCode = null,schoolCommunicationMetricCollectionCode=null;
		addPupilsDetails(report);
		addMetricsLabelValueCollection(report, studentClassroomSessionDivision,StringUtils.startsWith(levelNameCode, "G") ? 
				SchoolConstant.Code.MetricCollection.ATTENDANCE_STUDENT : SchoolConstant.Code.MetricCollection.ATTENDANCE_KINDERGARTEN_STUDENT);
		
		FormatArguments formatArguments = new FormatArguments();
		formatArguments.setIsRank(Boolean.TRUE);
		formatArguments.setType(CharacterSet.LETTER);
		String nameFormat = numberBusiness.format(studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber(), formatArguments).toUpperCase();
		nameFormat += " TERM , %s REPORT %s";
	
		if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.PK,SchoolConstant.Code.LevelName.K1,SchoolConstant.Code.LevelName.K2
				,SchoolConstant.Code.LevelName.K3},levelNameCode)){
			report.setName(String.format(nameFormat, studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel()
					.getLevelName().getName(),"SHEET"));
			schoolCommunicationMetricCollectionCode = SchoolConstant.Code.MetricCollection.COMMUNICATION_KINDERGARTEN_STUDENT;
			if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.PK,SchoolConstant.Code.LevelName.K1},levelNameCode)){
				effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT;
				if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.PK},levelNameCode)){
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_EXPRESSIVE_LANGUAGE);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_RECEPTIVE_LANGUAGE);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_READING_READNESS);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_NUMERACY_DEVELOPMENT);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_ARTS_MUSIC);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_SOCIAL_EMOTIONAL_DEVELOPMENT);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_GROSS_MOTOR_SKILLS);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_FINE_MOTOR_SKILLS);
					effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT;
				}else if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.K1},levelNameCode)){
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_ENGLISH_LANGUAGE_ARTS_READING);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_COMMUNICATION_SKILLS);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_SCIENCE);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_SOCIAL_STUDIES);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_MATHEMATICS);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_WORK_HABITS);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_SOCIAL_SKILLS);
					effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT;
				}
			}else if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.K2,SchoolConstant.Code.LevelName.K3},levelNameCode)){
				effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT;
				if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.K2,SchoolConstant.Code.LevelName.K3},levelNameCode)){
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_READING_READINESS);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_READING);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_WRITING);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_LISTENING_SPEAKING_VIEWING);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_ALPHABET_IDENTIFICATION);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_MATHEMATICS);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_SCIENCE_SOCIAL_STUDIES_MORAL_EDUCATION);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_ART_CRAFT);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_MUSIC);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_PHYSICAL_EDUCATION);
					addMetricsLabelValueCollection(report, studentClassroomSessionDivision,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_WORK_BEHAVIOUR_HABITS);
					
				}
			}
		}else{
			String studentBehaviourMetricCollectionCode = null;
			report.setName(String.format(nameFormat, studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel()
					.getLevelName().getName(),"CARD"));
			//r.setSubjectsBlockTitle("COGNITIVE ASSESSMENT");
			String testCoef = null,examCoef = "";	
			if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.G1,SchoolConstant.Code.LevelName.G2,SchoolConstant.Code.LevelName.G3},levelNameCode)){
				report.setName(String.format(nameFormat, "LOWER PRIMARY","CARD"));
				testCoef = "15";
				examCoef = "70";
				studentBehaviourMetricCollectionCode = SchoolConstant.Code.MetricCollection.BEHAVIOUR_PRIMARY_STUDENT;
				effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_PRIMARY_STUDENT;
			}else if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.G4,SchoolConstant.Code.LevelName.G5,SchoolConstant.Code.LevelName.G6},levelNameCode)){
				report.setName(String.format(nameFormat, "UPPER PRIMARY","CARD"));
				testCoef = "15";
				examCoef = "70";
				studentBehaviourMetricCollectionCode = SchoolConstant.Code.MetricCollection.BEHAVIOUR_PRIMARY_STUDENT;
				effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_PRIMARY_STUDENT;
			}else if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.G7,SchoolConstant.Code.LevelName.G8,SchoolConstant.Code.LevelName.G9},levelNameCode)){
				report.setName(String.format(nameFormat, "JUNIOR HIGH SCHOOL","CARD"));
				testCoef = "20";
				examCoef = "60";
				studentBehaviourMetricCollectionCode = SchoolConstant.Code.MetricCollection.BEHAVIOUR_SECONDARY_STUDENT;
				effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_SECONDARY_STUDENT;
			}else if(ArrayUtils.contains(new String[]{SchoolConstant.Code.LevelName.G10,SchoolConstant.Code.LevelName.G11,SchoolConstant.Code.LevelName.G12},levelNameCode)){
				report.setName(String.format(nameFormat, "SENIOR HIGH SCHOOL","CARD"));
				testCoef = "20";
				examCoef = "60";
				studentBehaviourMetricCollectionCode = SchoolConstant.Code.MetricCollection.BEHAVIOUR_SECONDARY_STUDENT;
				effortLevelsIntervalCollectionCode = SchoolConstant.Code.IntervalCollection.BEHAVIOUR_SECONDARY_STUDENT;
			}
			schoolCommunicationMetricCollectionCode = SchoolConstant.Code.MetricCollection.COMMUNICATION_STUDENT;
			report.addSubjectsTableColumnNames("No.","SUBJECTS","TEST 1 "+testCoef+"%","TEST 2 "+testCoef+"%","EXAM "+examCoef+"%","TOTAL 100%","GRADE","RANK","OUT OF","MAX","CLASS AVERAGE","REMARKS","TEACHER");
			
			labelValueCollectionReport = new LabelValueCollectionReport();
			labelValueCollectionReport.setName("OVERALL RESULT");
			labelValueCollectionReport.add("AVERAGE",report.getAverage());
			labelValueCollectionReport.add("GRADE",report.getAverageScale());
			if(Boolean.TRUE.equals(studentClassroomSessionDivision.getClassroomSessionDivision().getStudentRankable()))
				labelValueCollectionReport.add("RANK",report.getRank());
			report.addLabelValueCollection(labelValueCollectionReport);
			addMetricsLabelValueCollection(report, ((StudentClassroomSessionDivision)report.getSource()), studentBehaviourMetricCollectionCode);
			report.getCurrentLabelValueCollection().setName(StringUtils.upperCase(report.getCurrentLabelValueCollection().getName()));
			labelValueCollectionReport = new LabelValueCollectionReport();
			labelValueCollectionReport.setName(report.getCurrentLabelValueCollection().getName());
			labelValueCollectionReport.setCollection(report.getCurrentLabelValueCollection().getCollection().subList(6, 12));
			report.getCurrentLabelValueCollection().setCollection(report.getCurrentLabelValueCollection().getCollection().subList(0, 6));
			
			report.addLabelValueCollection(labelValueCollectionReport);
			
			addIntervalCollectionLabelValueCollection(report,inject(ClassroomSessionBusiness.class).findCommonNodeInformations(
				((StudentClassroomSessionDivision)report.getSource()).getClassroomSessionDivision().getClassroomSession()).getStudentClassroomSessionDivisionAverageScale()
				,null,Boolean.FALSE,Boolean.TRUE,new Integer[][]{{1,2}});
			report.getCurrentLabelValueCollection().setName(StringUtils.upperCase(report.getCurrentLabelValueCollection().getName()));		
		}
		
		addIntervalCollectionLabelValueCollection(report,inject(IntervalCollectionDao.class).read(effortLevelsIntervalCollectionCode),null,Boolean.TRUE,Boolean.FALSE,null);
		addSchoolCommunications(report, studentClassroomSessionDivision,schoolCommunicationMetricCollectionCode);
		return report;
	}
	
	private void addPupilsDetails(StudentClassroomSessionDivisionReportTemplateFile report){
		report.addLabelValueCollection("PUPIL'S DETAILS",new String[][]{
			{"Formname(s)", report.getStudent().getPerson().getNames()}
			,{"Surname", report.getStudent().getPerson().getGlobalIdentifier().getName()}
			,{"Date of birth", report.getStudent().getPerson().getGlobalIdentifier().getExistencePeriod().getFrom()}
			,{"Place of birth", report.getStudent().getPerson().getGlobalIdentifier().getBirthLocation()}
			,{"Admission No", report.getStudent().getGlobalIdentifier().getCode()}
			,{"Class", report.getClassroomSessionDivision().getClassroomSession().getName()}
			,{"Gender", report.getStudent().getPerson().getSex()}
			});
	}
	
	private void addSchoolCommunications(StudentClassroomSessionDivisionReportTemplateFile report,StudentClassroomSessionDivision studentClassroomSessionDivision,String metricCollectionCode){
		LabelValueCollectionReport labelValueCollectionReport = addMetricsLabelValueCollection(report, studentClassroomSessionDivision,metricCollectionCode);
		if(studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber()==inject(ClassroomSessionBusiness.class)
				.findCommonNodeInformations(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession()).getClassroomSessionDivisionOrderNumberInterval().getHigh().getValue().intValue()){
			StudentResults classroomSessionResults = inject(StudentClassroomSessionDao.class)
					.readByStudentByClassroomSession(studentClassroomSessionDivision.getStudent(), studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession()).getResults();
			
			labelValueCollectionReport.add("ANNUAL AVERAGE",format(classroomSessionResults.getEvaluationSort().getAverage().getValue()));
			labelValueCollectionReport.add("ANNUAL GRADE"
					,classroomSessionResults.getEvaluationSort().getAverageAppreciatedInterval()==null?NULL_VALUE:inject(IntervalBusiness.class)
							.findRelativeCode(classroomSessionResults.getEvaluationSort().getAverageAppreciatedInterval()));
			labelValueCollectionReport.add("ANNUAL RANK",inject(MathematicsBusiness.class).format(classroomSessionResults.getEvaluationSort().getRank()));
			labelValueCollectionReport.add("PROMOTION INFORMATION",
					classroomSessionResults.getEvaluationSort().getAveragePromotedInterval()==null?NULL_VALUE:classroomSessionResults.getEvaluationSort()
							.getAveragePromotedInterval().getName().toUpperCase());
			labelValueCollectionReport.add("NEXT ACADEMIC SESSION",format(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession()
					.getAcademicSession().getNextStartingDate()));
			
		}else{
			ClassroomSessionDivision nextClassroomSessionDivision = inject(ClassroomSessionDivisionDao.class)
					.readByClassroomSessionByOrderNumber(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession()
							,(studentClassroomSessionDivision.getClassroomSessionDivision().getOrderNumber()));
		
			labelValueCollectionReport.add("NEXT OPENING",format(nextClassroomSessionDivision.getBirthDate()));
			labelValueCollectionReport.add("NEXT TERM EXAMINATION",format(nextClassroomSessionDivision.getDeathDate()));
		}
	}
	
	*/
}