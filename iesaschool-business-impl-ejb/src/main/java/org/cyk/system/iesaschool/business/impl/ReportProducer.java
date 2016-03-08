package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments.CharacterSet;
import org.cyk.system.root.model.file.report.LabelValueCollectionReport;
import org.cyk.system.root.model.userinterface.style.FontName;
import org.cyk.system.root.model.userinterface.style.Style;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.session.StudentClassroomSessionDivisionReport;

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
		LabelValueCollectionReport labelValueCollectionReport;
		StudentClassroomSessionDivisionReport r = super.produceStudentClassroomSessionDivisionReport(studentClassroomSessionDivision,parameters);
		r.getAcademicSession().getCompany().setName(iesa("INTERNATIONAL ")+iesa("ENGLISH ")+iesa("SCHOOL OF ")+iesa("ABIDJAN"));
		
		AcademicSession as = studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getAcademicSession();
		r.getAcademicSession().setFromDateToDate(timeBusiness.findYear(as.getPeriod().getFromDate())+"/"+timeBusiness.findYear(as.getPeriod().getToDate())+" ACADEMIC SESSION");
	
		r.addLabelValueCollection("PUPIL'S DETAILS",new String[][]{
				{"Formname(s)", r.getStudent().getPerson().getNames()}
				,{"Surname", r.getStudent().getPerson().getSurname()}
				,{"Date of birth", r.getStudent().getPerson().getBirthDate()}
				,{"Place of birth", r.getStudent().getPerson().getBirthLocation()}
				,{"Admission No", r.getStudent().getRegistrationCode()}
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
		String name = numberBusiness.format(studentClassroomSessionDivision.getClassroomSessionDivision().getIndex()+1, formatArguments).toUpperCase();
		r.setName(name+" TERM , "+studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel().getGroup().getName().toUpperCase()
				+" REPORT");
		
		String levelNameCode = studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getLevelTimeDivision().getLevel().getName().getCode();
		if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_PK,IesaConstant.LEVEL_NAME_CODE_K1,IesaConstant.LEVEL_NAME_CODE_K2,IesaConstant.LEVEL_NAME_CODE_K3},levelNameCode)){
			r.setName(r.getName()+" SHEET");
			String performanceCodeMetricCollectionCode = null;
			if(IesaConstant.LEVEL_NAME_CODE_PK.equals(levelNameCode)){
				addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), new String[]{
						performanceCodeMetricCollectionCode = IesaConstant.MERIC_COLLECTION_PK_STUDENT_EXPRESSIVE_LANGUAGE
						, IesaConstant.MERIC_COLLECTION_PK_STUDENT_RECEPTIVE_LANGUAGE
						, IesaConstant.MERIC_COLLECTION_PK_STUDENT_READING_READNESS
						, IesaConstant.MERIC_COLLECTION_PK_STUDENT_NUMERACY_DEVELOPMENT
						, IesaConstant.MERIC_COLLECTION_PK_STUDENT_ARTS_MUSIC
						, IesaConstant.MERIC_COLLECTION_PK_STUDENT_SOCIAL_EMOTIONAL_DEVELOPMENT
						, IesaConstant.MERIC_COLLECTION_PK_STUDENT_GROSS_MOTOR_SKILLS
						, IesaConstant.MERIC_COLLECTION_PK_STUDENT_FINE_MOTOR_SKILLS
					});
			}else if(IesaConstant.LEVEL_NAME_CODE_K1.equals(levelNameCode)){
				addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), new String[]{
						performanceCodeMetricCollectionCode = IesaConstant.MERIC_COLLECTION_K1_STUDENT_ENGLISH_LANGUAGE_ARTS_READING
						, IesaConstant.MERIC_COLLECTION_K1_STUDENT_COMMUNICATION_SKILLS
						, IesaConstant.MERIC_COLLECTION_K1_STUDENT_SCIENCE
						, IesaConstant.MERIC_COLLECTION_K1_STUDENT_SOCIAL_STUDIES
						, IesaConstant.MERIC_COLLECTION_K1_STUDENT_MATHEMATICS
						, IesaConstant.MERIC_COLLECTION_K1_STUDENT_WORK_HABITS
						, IesaConstant.MERIC_COLLECTION_K1_STUDENT_SOCIAL_SKILLS
					});
			}else if(IesaConstant.LEVEL_NAME_CODE_K2.equals(levelNameCode) || IesaConstant.LEVEL_NAME_CODE_K3.equals(levelNameCode)){
				addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), new String[]{
						performanceCodeMetricCollectionCode = IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_READING_READINESS
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_READING
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_WRITING
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_LISTENING_SPEAKING_VIEWING
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_ALPHABET_IDENTIFICATION
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_MATHEMATICS
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_SCIENCE_SOCIAL_STUDIES_MORAL_EDUCATION
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_ART_CRAFT
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_MUSIC
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_PHYSICAL_EDUCATION
						, IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_WORK_BEHAVIOUR_HABITS
					});
			}
			
			labelValueCollectionReport = addIntervalCollectionLabelValueCollection(r,rootBusinessLayer.getMetricCollectionDao().read(performanceCodeMetricCollectionCode).getValueIntervalCollection()
					,Boolean.TRUE,Boolean.FALSE,null);
			labelValueCollectionReport.add("NA", "Not Assessed");
		}else{
			String studentBehaviourMetricCollectionCode = null;
			r.setName(r.getName()+" CARD");
			String testCoef = null,examCoef = "";	
			if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G1,IesaConstant.LEVEL_NAME_CODE_G2,IesaConstant.LEVEL_NAME_CODE_G3},levelNameCode)){
				name += " LOWER";
				testCoef = "15";
				examCoef = "70";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G1_G6_STUDENT_BEHAVIOUR;
			}else if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G4,IesaConstant.LEVEL_NAME_CODE_G5,IesaConstant.LEVEL_NAME_CODE_G6},levelNameCode)){
				name += " UPPER";
				testCoef = "15";
				examCoef = "70";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G1_G6_STUDENT_BEHAVIOUR;
			}else if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G7,IesaConstant.LEVEL_NAME_CODE_G8,IesaConstant.LEVEL_NAME_CODE_G9},levelNameCode)){
				name += " JUNIOR HIGH SCHOOL";
				testCoef = "20";
				examCoef = "60";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G7_G12_STUDENT_BEHAVIOUR;
			}else if(ArrayUtils.contains(new String[]{IesaConstant.LEVEL_NAME_CODE_G10,IesaConstant.LEVEL_NAME_CODE_G11,IesaConstant.LEVEL_NAME_CODE_G12},levelNameCode)){
				name += " SENIOR HIGH SCHOOL";
				testCoef = "20";
				examCoef = "60";
				studentBehaviourMetricCollectionCode = IesaConstant.MERIC_COLLECTION_G7_G12_STUDENT_BEHAVIOUR;
			}
			
			r.addSubjectsTableColumnNames("No.","SUBJECTS","Test 1 "+testCoef+"%","Test 2 "+testCoef+"%","Exam "+examCoef+"%","TOTAL 100%","GRADE","RANK","OUT OF","MAX","CLASS AVERAGE","REMARKS","TEACHER");
			
			labelValueCollectionReport = new LabelValueCollectionReport();
			labelValueCollectionReport.setName("OVERALL RESULT");
			labelValueCollectionReport.add("AVERAGE",r.getAverage());
			labelValueCollectionReport.add("GRADE",r.getAverageScale());
			if(Boolean.TRUE.equals(studentClassroomSessionDivision.getClassroomSessionDivision().getStudentRankable()))
				labelValueCollectionReport.add("RANK",r.getRank());
			r.addLabelValueCollection(labelValueCollectionReport);
			
			addStudentResultsLabelValueCollection(r, ((StudentClassroomSessionDivision)r.getSource()).getResults(), studentBehaviourMetricCollectionCode);
			labelValueCollectionReport = new LabelValueCollectionReport();
			labelValueCollectionReport.setName(r.getCurrentLabelValueCollection().getName());
			labelValueCollectionReport.setCollection(r.getCurrentLabelValueCollection().getCollection().subList(6, 12));
			r.getCurrentLabelValueCollection().setCollection(r.getCurrentLabelValueCollection().getCollection().subList(0, 6));
			
			r.addLabelValueCollection(labelValueCollectionReport);
			
			addIntervalCollectionLabelValueCollection(r,SchoolBusinessLayer.getInstance().getClassroomSessionBusiness().findCommonNodeInformations(
				((StudentClassroomSessionDivision)r.getSource()).getClassroomSessionDivision().getClassroomSession()).getStudentClassroomSessionDivisionAverageScale()
				,Boolean.FALSE,Boolean.TRUE,new Integer[][]{{1,2}});
			
			addIntervalCollectionLabelValueCollection(r,rootBusinessLayer.getMetricCollectionDao().read(IesaConstant.MERIC_COLLECTION_G1_G6_STUDENT_BEHAVIOUR).getValueIntervalCollection()
					,Boolean.TRUE,Boolean.FALSE,null);
			
		}
		
		if(studentClassroomSessionDivision.getClassroomSessionDivision().getIndex()==3){
			labelValue("school.report.studentclassroomsessiondivision.block.informations.annualaverage", "To Compute");
			labelValue("school.report.studentclassroomsessiondivision.block.informations.annualgrade", "To Compute");
			labelValue("school.report.studentclassroomsessiondivision.block.informations.annualrank", "To Compute");
			//labelValue("school.report.studentclassroomsessiondivision.block.informations.promotion", 
			//		studentClassroomSessionDivision.get "To Compute");
			labelValue("school.report.studentclassroomsessiondivision.block.informations.nextacademicsession", 
					format(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession().getAcademicSession().getNextStartingDate()));
		}else{
			ClassroomSessionDivision nextClassroomSessionDivision = SchoolBusinessLayer.getInstance().getClassroomSessionDivisionDao()
					.readByClassroomSessionByIndex(studentClassroomSessionDivision.getClassroomSessionDivision().getClassroomSession()
							,new Byte((byte) (studentClassroomSessionDivision.getClassroomSessionDivision().getIndex()+1)));
		
			r.addLabelValueCollection("HOME/SCHOOL COMMUNICATIONS",new String[][]{
				{"CONFERENCE REQUESTED",studentClassroomSessionDivision.getResults().getConferenceRequested()==null?"NO"
						:studentClassroomSessionDivision.getResults().getConferenceRequested()?"YES":"NO"}
				,{"NEXT OPENING",format(nextClassroomSessionDivision.getPeriod().getFromDate())}
				,{"NEXT TERM EXAMINATION",format(nextClassroomSessionDivision.getPeriod().getToDate())}
				});
		}
	
		
		
		return r;
	}
		
}