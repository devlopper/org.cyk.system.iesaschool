package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyBusinessLayerAdapter;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootDataProducerHelper.RootDataProducerHelperListener;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.mathematics.MetricCollection;
import org.cyk.system.root.model.mathematics.MetricValueType;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.persistence.api.GenericDao;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.session.SchoolReportProducer;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectEvaluationTypeBusiness;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.SchoolDataProducerHelper;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.CommonNodeInformations;
import org.cyk.system.school.model.session.Level;
import org.cyk.system.school.model.session.LevelGroup;
import org.cyk.system.school.model.session.LevelGroupType;
import org.cyk.system.school.model.session.LevelName;
import org.cyk.system.school.model.session.LevelTimeDivision;
import org.cyk.system.school.model.session.School;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubjectEvaluationType;
import org.cyk.system.school.model.subject.EvaluationType;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.joda.time.DateTimeConstants;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=IesaBusinessLayer.DEPLOYMENT_ORDER) @Getter
public class IesaBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = SchoolBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static IesaBusinessLayer INSTANCE;
	
	@Inject private RootBusinessLayer rootBusinessLayer;
	@Inject private RootRandomDataProvider rootRandomDataProvider;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private SchoolBusinessLayer schoolBusinessLayer;
	@Inject private SchoolDataProducerHelper schoolDataProducerHelper;
	
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject private CompanyBusiness companyBusiness;
	@Inject private ClassroomSessionDivisionSubjectEvaluationTypeBusiness subjectEvaluationTypeBusiness;
	@Inject private ClassroomSessionBusiness classroomSessionBusiness;
	@Inject private ClassroomSessionDivisionBusiness classroomSessionDivisionBusiness;
	@Inject private ClassroomSessionDivisionSubjectBusiness classroomSessionDivisionSubjectBusiness;
	
	@Inject private GenericDao genericDao;
	
	private EvaluationType evaluationTypeTest1,evaluationTypeTest2,evaluationTypeExam;
	
	private MetricCollection studentWorkMetricCollectionPk,studentWorkMetricCollectionK1,studentWorkMetricCollectionK2,studentWorkMetricCollectionK3
		,studentWorkMetricCollectionG1G6,studentWorkMetricCollectionG7G12;
	
	private ArrayList<Subject> subjectsG1G3 = new ArrayList<>(),subjectsG4G6 = new ArrayList<>()
			,subjectsG7G9 = new ArrayList<>(),subjectsG10G12 = new ArrayList<>(); 
	private LevelGroupType levelGroupType;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		/*rootBusinessLayer.getBusinessLayerListeners().add(new BusinessLayerListener.Adapter.Default(){
			
			private static final long serialVersionUID = -9212697312172717454L;
			@Override
			public void beforeInstall(BusinessLayer businessLayer,Installation installation) {
				installation.getApplication().setName("IESA WebApp");
				super.beforeInstall(businessLayer, installation);
			}
		});*/
		
		companyBusinessLayer.getCompanyBusinessLayerListeners().add(new CompanyBusinessLayerAdapter() {
			private static final long serialVersionUID = 5179809445850168706L;

			@Override
			public String getCompanyName() {
				return "IESA";
			}
			
			@Override
			public byte[] getCompanyLogoBytes() {
				return getResourceAsBytes(IesaBusinessLayer.class.getPackage(),"image/logo.png");
			}
			
			@Override
			public void handleCompanyToInstall(Company company) {
				super.handleCompanyToInstall(company);
				addContacts(company.getContactCollection(), new String[]{"RueJ7 1-II Plateux Vallon, Cocody"}, new String[]{"22417217","21014459"}
				, new String[]{"05996283","49925138","06173731"}, new String[]{"08 BP 1828 Abidjan 08"}, new String[]{"iesa@aviso.ci"}, new String[]{"http://www.iesaci.com"});
			}
		});
		
		schoolBusinessLayer.setReportProducer(new ReportProducer());
    	
		rootDataProducerHelper.getRootDataProducerHelperListeners().add(new RootDataProducerHelperListener.Adapter.Default(){
			private static final long serialVersionUID = 1L;
			@Override
			public void set(Object object) {
				super.set(object);
				if(object instanceof LevelName){
					;//((LevelName)object).setNodeInformations(commonNodeInformations);
				}else if(object instanceof LevelGroup){
					((LevelGroup)object).setType(levelGroupType);
				}
			}
		});
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void structure(){
		levelGroupType = create(schoolBusinessLayer.getLevelGroupTypeBusiness().instanciateOne("LevelGroupTypeDummy"));
		LevelGroup levelGroupPrimary = (LevelGroup) create(schoolBusinessLayer.getLevelGroupBusiness().instanciateOne(SchoolConstant.LEVEL_GROUP_PRIMARY)
				.setType(levelGroupType));
		LevelGroup levelGroupSecondary = (LevelGroup) create(schoolBusinessLayer.getLevelGroupBusiness().instanciateOne(SchoolConstant.LEVEL_GROUP_SECONDARY)
				.setType(levelGroupType));
		
    	evaluationTypeTest1 = create(schoolBusinessLayer.getEvaluationTypeBusiness().instanciateOne(IesaConstant.EVALUATION_TYPE_TEST1,"Test 1"));
    	evaluationTypeTest2 = create(schoolBusinessLayer.getEvaluationTypeBusiness().instanciateOne(IesaConstant.EVALUATION_TYPE_TEST2,"Test 2"));
    	evaluationTypeExam = create(schoolBusinessLayer.getEvaluationTypeBusiness().instanciateOne(IesaConstant.EVALUATION_TYPE_EXAM,"Exam"));
    	
    	studentWorkMetricCollectionPk = rootBusinessLayer.getMetricCollectionBusiness().instanciateOne("BSWHPk","Behaviour,Study and Work Habits",MetricValueType.NUMBER
    			, new String[]{"Participates actively during circle time","Participates in singing rhymes","Can say her name and name of classmates"
    			,"Can respond appropriately to “how are you?”","Can say his/her age","Can say the name of her school","Names objects in the classroom and school environment"
    			,"Uses at least one of the following words “me”,“I”, “he”, “she”, “you”","Talks in two or three word phrases and longer sentences"
    			,"Can use “and” to connect words/phrases","Talks with words in correct order","Can be engaged in conversations"}
    	, new String[][]{ {"BSWHPk_1", "Learning to do", "1", "1"},{"BSWHPk_2", "Does sometimes", "2", "2"} ,{"BSWHPk_3", "Does regularly", "3", "3"} });
    	
    	studentWorkMetricCollectionG1G6 = rootBusinessLayer.getMetricCollectionBusiness().instanciateOne("BSWHG1G6","Behaviour,Study and Work Habits",MetricValueType.NUMBER
    			, new String[]{"Respect authority","Works independently and neatly","Completes homework and class work on time","Shows social courtesies","Demonstrates self-control"
    					,"Takes care of school and others materials","Game/Sport","Handwriting","Drawing/Painting","Punctionality/Regularity","Works cooperatively in groups"
    					,"Listens and follows directions"}
    	, new String[][]{ {"1", "Has no regard for the observable traits", "1", "1"},{"2", "Shows minimal regard for the observable traits", "2", "2"}
    	,{"3", "Acceptable level of observable traits", "3", "3"},{"4", "Maintains high level of observable traits", "4", "4"}
    	,{"5", "Maintains an excellent degree of observable traits", "5", "5"} });
   
    	studentWorkMetricCollectionG7G12 = rootBusinessLayer.getMetricCollectionBusiness().instanciateOne("BSWHG7G12","Behaviour,Study and Work Habits",MetricValueType.STRING
    			, new String[]{"Respect authority","Works independently and neatly","Completes homework and class work on time","Shows social courtesies","Demonstrates self-control"
    					,"Takes care of school and others materials","Game/Sport","Handwriting","Drawing/Painting","Punctionality/Regularity","Works cooperatively in groups"
    					,"Listens and follows directions"}
    	, new String[][]{ {"E", "Excellent", "1", "1"},{"G", "Good", "2", "2"}
    	,{"S", "Satisfactory", "3", "3"},{"N", "Needs Improvement", "4", "4"}
    	,{"H", "Has no regard", "5", "5"} });
    	
    	File reportHeaderFile = createFile("image/document_header.png");
    	
		File reportFile = createFile("report/studentclassroomsessiondivision.jrxml", "studentclassroomsessiondivisionreport.jrxml");
		ReportTemplate reportTemplate = new ReportTemplate("SCSDRT",reportFile,reportHeaderFile,createFile("image/studentclassroomsessiondivisionreport_background.jpg"));
		create(reportTemplate);
		
		File reportFilePk = createFile("report/pkg.jrxml", "studentclassroomsessiondivision_pkg.jrxml");
		ReportTemplate reportTemplatePk = new ReportTemplate("SCSDRTPK",reportFilePk,reportHeaderFile,createFile("image/studentclassroomsessiondivisionreport_background.jpg"));
		create(reportTemplatePk);
		
		CommonNodeInformations commonNodeInformationsPk = schoolDataProducerHelper.instanciateOneCommonNodeInformations(null, reportTemplatePk, TimeDivisionType.DAY, TimeDivisionType.TRIMESTER, "1");
		
		CommonNodeInformations commonNodeInformationsG1G3 = schoolDataProducerHelper.instanciateOneCommonNodeInformations(create(rootBusinessLayer.getIntervalCollectionBusiness()
				.instanciateOne("ICEV1", "ICEV1", new String[][]{
						{"A+", "Excellent", "90", "100"},{"A", "Very good", "80", "89.99"},{"B+", "Good", "70", "79.99"},{"B", "Fair", "60", "69.99"}
						,{"C+", "Satisfactory", "55", "59.99"},{"C", "Barely satisfactory", "50", "54.99"},{"E", "Fail", "0", "49.99"}})), reportTemplate
						, TimeDivisionType.DAY, TimeDivisionType.TRIMESTER, "1");	
		CommonNodeInformations commonNodeInformationsG4G6 = commonNodeInformationsG1G3;
		
		CommonNodeInformations commonNodeInformationsG7G9 = schoolDataProducerHelper.instanciateOneCommonNodeInformations(create(rootBusinessLayer.getIntervalCollectionBusiness()
				.instanciateOne("ICEV2", "ICEV2", new String[][]{
						{"A*", "Outstanding", "90", "100"},{"A", "Excellent", "80", "89.99"},{"B", "Very Good", "70", "79.99"},{"C", "Good", "60", "69.99"}
						,{"D", "Satisfactory", "50", "59.99"},{"E", "Fail", "0", "49.99"}})), reportTemplate
						, TimeDivisionType.DAY, TimeDivisionType.TRIMESTER, "1");	
		CommonNodeInformations commonNodeInformationsG10G12 = commonNodeInformationsG7G9;
		
		School school = new School(ownedCompanyBusiness.findDefaultOwnedCompany(),commonNodeInformationsG1G3);
    	create(school);
    	
    	//school.getOwnedCompany().getCompany().setManager(rootRandomDataProvider.oneFromDatabase(Person.class));
    	//companyBusiness.update(school.getOwnedCompany().getCompany());
    	
    	AcademicSession academicSession = new AcademicSession(school,commonNodeInformationsG1G3,new Date());
    	academicSession.getPeriod().setFromDate(new Date());
    	academicSession.getPeriod().setToDate(new Date(academicSession.getPeriod().getFromDate().getTime()+DateTimeConstants.MILLIS_PER_DAY*355));
    	academicSession = create(academicSession);
		
		// Subjects
    	schoolDataProducerHelper.createOneSubject("Mathematics",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("Grammar",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("Reading & Comprehension",new ArrayList[]{subjectsG1G3});
    	schoolDataProducerHelper.createOneSubject("Hand writing",new ArrayList[]{subjectsG1G3});
    	schoolDataProducerHelper.createOneSubject("Spelling",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("Phonics",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("Creative writing",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("Moral education",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("Social Studies",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("Science",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("French",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("Art & Craft",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("Music",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("ICT",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("Physical education",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	
    	schoolDataProducerHelper.createOneSubject("Litterature",new ArrayList[]{subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("Comprehension",new ArrayList[]{subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject("History",new ArrayList[]{subjectsG4G6,subjectsG7G9,subjectsG10G12});
    	
    	schoolDataProducerHelper.createOneSubject("English Language",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject("Literature in english",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject("Geography",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject("Physics",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject("Chemistry",new ArrayList[]{subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("Biology",new ArrayList[]{subjectsG7G9});
    	schoolDataProducerHelper.createOneSubject("Spanish",new ArrayList[]{subjectsG7G9});
    	
    	schoolDataProducerHelper.createOneSubject("Sociology",new ArrayList[]{subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject("Religious studies/Divinity",new ArrayList[]{subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject("Core mathematics",new ArrayList[]{subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject("Advanced mathematics",new ArrayList[]{subjectsG10G12});
				
    	Collection<ClassroomSession> classroomSessions = new ArrayList<>(); 
    	Collection<ClassroomSessionDivision> classroomSessionDivisions = new ArrayList<>(); 
    	Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects = new ArrayList<>();
    	Collection<ClassroomSessionDivisionSubjectEvaluationType> subjectEvaluationTypes = new ArrayList<>(); 
    	
    	Integer gradeIndex = 0;
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_PK,"Pre-Kindergarten",levelGroupPrimary,commonNodeInformationsPk,gradeIndex++) ,null, null,null,Boolean.FALSE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_K1,"Kindergarten 1",levelGroupPrimary,commonNodeInformationsPk,gradeIndex++) , null,null,null,Boolean.FALSE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_K2,"Kindergarten 2",levelGroupPrimary,commonNodeInformationsPk,gradeIndex++) , null,null,null,Boolean.FALSE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_K3,"Kindergarten 3",levelGroupPrimary,commonNodeInformationsPk,gradeIndex++) , null,null,null,Boolean.FALSE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G1,"Grade 1",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) ,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}}, subjectsG1G3,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G2,"Grade 2",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) ,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}}, subjectsG1G3,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G3,"Grade 3",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) ,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG1G3,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G4,"Grade 4",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) ,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G5,"Grade 5",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) ,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G6,"Grade 6",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) ,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6,null,Boolean.TRUE,Boolean.TRUE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G7,"Grade 7",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) , new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG7G9,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G8,"Grade 8",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) , new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG7G9,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G9,"Grade 9",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) ,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG7G9,null,Boolean.TRUE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G10,"Grade 10",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) , new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG10G12,null,Boolean.TRUE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G11,"Grade 11",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) , new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG10G12,null,Boolean.TRUE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G12,"Grade 12",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) , new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG10G12,null,Boolean.TRUE,Boolean.FALSE);
    	
    	classroomSessionBusiness.create(classroomSessions);
    	classroomSessionDivisionBusiness.create(classroomSessionDivisions);
    	classroomSessionDivisionSubjectBusiness.create(classroomSessionDivisionSubjects);
    	System.out.println("SSS : "+classroomSessionDivisionSubjects);
    	subjectEvaluationTypeBusiness.create(subjectEvaluationTypes);
    	
    	for(AbstractIdentifiable identifiable :  genericDao.use(EvaluationType.class).select().all())
    		SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.getEvaluationTypeCodes().add(((EvaluationType)identifiable).getCode());
    	SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.setSumMarks(Boolean.TRUE);
	}
		
	@Override
	protected void persistData() {
		structure();
	}
	
	@Override
    public void registerTypedBusinessBean(Map<Class<AbstractIdentifiable>, TypedBusiness<AbstractIdentifiable>> beansMap) {}
	
	/**/
	
	@Override
	protected void setConstants(){}
	
	public static IesaBusinessLayer getInstance() {
		return INSTANCE;
	}

	/**/
	
	protected void fakeTransactions(){}
	
	/**/
	
	
	
}
