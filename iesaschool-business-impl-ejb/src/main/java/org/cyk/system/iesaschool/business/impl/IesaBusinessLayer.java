package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;

import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyBusinessLayerAdapter;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.api.mathematics.IntervalBusiness;
import org.cyk.system.root.business.api.mathematics.IntervalCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.MetricCollectionBusiness;
import org.cyk.system.root.business.api.network.UniformResourceLocatorBusiness;
import org.cyk.system.root.business.api.party.person.PersonBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.mathematics.Interval;
import org.cyk.system.root.model.mathematics.IntervalCollection;
import org.cyk.system.root.model.mathematics.MetricCollection;
import org.cyk.system.root.model.mathematics.MetricValueInputted;
import org.cyk.system.root.model.mathematics.MetricValueType;
import org.cyk.system.root.model.network.UniformResourceLocatorParameter;
import org.cyk.system.root.model.party.person.JobInformations;
import org.cyk.system.root.model.party.person.JobTitle;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.party.person.PersonExtendedInformations;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.root.model.party.person.Sex;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.security.Role;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.persistence.api.GenericDao;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionDivisionStudentsMetricCollectionBusiness;
import org.cyk.system.school.business.api.session.LevelGroupBusiness;
import org.cyk.system.school.business.api.session.LevelGroupTypeBusiness;
import org.cyk.system.school.business.api.session.SchoolReportProducer;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectEvaluationTypeBusiness;
import org.cyk.system.school.business.api.subject.EvaluationTypeBusiness;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.SchoolDataProducerHelper;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.ClassroomSessionDivisionStudentsMetricCollection;
import org.cyk.system.school.model.session.CommonNodeInformations;
import org.cyk.system.school.model.session.LevelGroup;
import org.cyk.system.school.model.session.LevelGroupType;
import org.cyk.system.school.model.session.School;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubjectEvaluationType;
import org.cyk.system.school.model.subject.Evaluation;
import org.cyk.system.school.model.subject.EvaluationType;
import org.cyk.system.school.model.subject.StudentClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.system.school.persistence.api.actor.TeacherDao;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.joda.time.DateTime;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=IesaBusinessLayer.DEPLOYMENT_ORDER) @Getter
public class IesaBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = SchoolBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static IesaBusinessLayer INSTANCE;
	
	@Inject private RootBusinessLayer rootBusinessLayer;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private SchoolBusinessLayer schoolBusinessLayer;
	@Inject private SchoolDataProducerHelper schoolDataProducerHelper;
	
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject private CompanyBusiness companyBusiness;
	@Inject private ClassroomSessionDivisionSubjectEvaluationTypeBusiness subjectEvaluationTypeBusiness;
	@Inject private ClassroomSessionBusiness classroomSessionBusiness;
	@Inject private ClassroomSessionDivisionBusiness classroomSessionDivisionBusiness;
	@Inject private ClassroomSessionDivisionSubjectBusiness classroomSessionDivisionSubjectBusiness;
	@Inject private ClassroomSessionDivisionStudentsMetricCollectionBusiness classroomSessionDivisionStudentsMetricCollectionBusiness;
	
	@Inject private GenericDao genericDao;
	@Inject private TeacherDao teacherDao;
	
	private EvaluationType evaluationTypeTest1,evaluationTypeTest2,evaluationTypeExam;
	
	private MetricCollection[] pkMetricCollections,k1MetricCollections,k2k3MetricCollections
		,g1g6MetricCollections,g7g12MetricCollections;
	
	private ArrayList<Subject> subjectsG1G3 = new ArrayList<>(),subjectsG4G6 = new ArrayList<>()
			,subjectsG7G8 = new ArrayList<>(),subjectsG9G12 = new ArrayList<>(); 
	
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
		SchoolDataProducerHelper.Listener.COLLECTION.add(new SchoolDataProducerHelper.Listener.Adapter(){
			private static final long serialVersionUID = -5322009577688489872L;
			@Override
			public void classroomSessionDivisionCreated(ClassroomSessionDivision classroomSessionDivision) {
				classroomSessionDivision.setStudentSubjectAttendanceAggregated(Boolean.FALSE);
			}
		});
		/*
		org.cyk.system.school.business.impl.session.StudentClassroomSessionDivisionBusinessImpl.Listener.COLLECTION.add(
				new org.cyk.system.school.business.impl.session.StudentClassroomSessionDivisionBusinessImpl.Listener.Adapter.Default(){
					private static final long serialVersionUID = 1L;

					@Override
					public void processOnEvaluationAverageUpdated(Collection<ClassroomSessionDivision> classroomSessionDivisions,BusinessServiceCallArguments<StudentClassroomSessionDivision> callArguments) {
						super.processOnEvaluationAverageUpdated(classroomSessionDivisions,callArguments);
						if(classroomSessionDivisions.iterator().next().getClassroomSession().getAcademicSession().getNodeInformations().getCurrentClassroomSessionDivisionIndex().intValue()==2){
							Collection<ClassroomSession> classroomSessions = new HashSet<>();
							for(ClassroomSessionDivision classroomSessionDivision : classroomSessionDivisions)
								classroomSessions.add(classroomSessionDivision.getClassroomSession());
							
							inject(Instance().getStudentClassroomSessionBusiness.class).updateAverage(classroomSessions, new BusinessServiceCallArguments<StudentClassroomSession>());
						
							RankOptions<SortableStudentResults> rankOptions = new RankOptions<>();
					    	 rankOptions.getSortOptions().setComparator(new SortableStudentResultsComparator(Boolean.TRUE));
					    	inject(Instance().getStudentClassroomSessionBusiness.class).updateRank(classroomSessions, rankOptions, new BusinessServiceCallArguments<StudentClassroomSession>());
						}else{
							
						}
					}
				});
		*/
		AbstractSchoolReportProducer.DEFAULT = new ReportProducer();
		PersonBusiness.FindNamesOptions.FIRST_NAME_IS_FIRST = Boolean.FALSE;
	}
	
	@Override @SuppressWarnings("unchecked")
	protected void persistStructureData(){
		updateEnumeration(PersonTitle.class, PersonTitle.MISTER, "Mr.");
		updateEnumeration(PersonTitle.class, PersonTitle.MISS, "Ms.");
		updateEnumeration(PersonTitle.class, PersonTitle.MADAM, "Mrs.");
		
		updateEnumeration(Sex.class, Sex.MALE, "Male");
		updateEnumeration(Sex.class, Sex.FEMALE, "Female");
		
		LevelGroupType levelGroupType = create(inject(LevelGroupTypeBusiness.class).instanciateOne("LevelGroupTypeDummy"));
		LevelGroup levelGroupKindergarten = (LevelGroup) create(inject(LevelGroupBusiness.class).instanciateOne(SchoolConstant.LEVEL_GROUP_KINDERGARTEN)
				.setType(levelGroupType));
		LevelGroup levelGroupPrimary = (LevelGroup) create(inject(LevelGroupBusiness.class).instanciateOne(SchoolConstant.LEVEL_GROUP_PRIMARY)
				.setType(levelGroupType));
		LevelGroup levelGroupSecondary = (LevelGroup) create(inject(LevelGroupBusiness.class).instanciateOne(SchoolConstant.LEVEL_GROUP_SECONDARY)
				.setType(levelGroupType));
		
    	evaluationTypeTest1 = create(inject(EvaluationTypeBusiness.class).instanciateOne(IesaConstant.EVALUATION_TYPE_TEST1,"Test 1"));
    	evaluationTypeTest2 = create(inject(EvaluationTypeBusiness.class).instanciateOne(IesaConstant.EVALUATION_TYPE_TEST2,"Test 2"));
    	evaluationTypeExam = create(inject(EvaluationTypeBusiness.class).instanciateOne(IesaConstant.EVALUATION_TYPE_EXAM,"Exam"));
    	
    	createMetricCollections();
    	
    	File reportHeaderFile = createFile("image/document_header.png");
    	File reportBackgroundFile = createFile("image/studentclassroomsessiondivisionreport_background.jpg");
    	
    	Interval interval = inject(IntervalBusiness.class).instanciateOne(null, "A", "1", "2");
		create(interval);
    	
    	CommonNodeInformations commonNodeInformationsPkg = instanciateCommonNodeInformations(null,null, reportHeaderFile, reportBackgroundFile, "pkg.jrxml", "2",interval);
    	CommonNodeInformations commonNodeInformationsKg1 = instanciateCommonNodeInformations(null,null, reportHeaderFile, reportBackgroundFile, "kg1.jrxml", "2",interval);
    	CommonNodeInformations commonNodeInformationsKg2Kg3 = instanciateCommonNodeInformations(null,null, reportHeaderFile, reportBackgroundFile, "kg2kg3.jrxml", "2",interval);
    	
    	ReportTemplate reportTemplate = createReportTemplate("SCSDRT","student classroomsession division report",Boolean.TRUE
    			, "report/studentclassroomsessiondivision/g1g12.jrxml", null, null, null);
    	/*
		File reportFile = createFile("report/studentclassroomsessiondivision/g1g12.jrxml", "studentclassroomsessiondivisionreport_g1g12.jrxml");
		ReportTemplate reportTemplate = new ReportTemplate("SCSDRT",reportFile,reportHeaderFile,createFile("image/studentclassroomsessiondivisionreport_background.jpg"),null);
		create(reportTemplate);
		*/
		CommonNodeInformations commonNodeInformationsG1G3 = instanciateCommonNodeInformations(create(inject(IntervalCollectionBusiness.class)
				.instanciateOne("ICEV1", "Grading Scale", new String[][]{
						{"A+", "Excellent", "90", "100"},{"A", "Very good", "80", "89.99"},{"B+", "Good", "70", "79.99"},{"B", "Fair", "60", "69.99"}
						,{"C+", "Satisfactory", "55", "59.99"},{"C", "Barely satisfactory", "50", "54.99"},{"E", "Fail", "0", "49.99"}})),
				create(inject(IntervalCollectionBusiness.class)
						.instanciateOne("ICP1", "Promotion Scale", new String[][]{
								{"P", "Promoted", "50", "100"},{"PT", "Promoted on trial", "45", "49.99"},{"NP", "Not promoted", "0", "44.99"}})),reportTemplate, "2",interval);
		CommonNodeInformations commonNodeInformationsG4G6 = commonNodeInformationsG1G3;
		
		CommonNodeInformations commonNodeInformationsG7G9 = instanciateCommonNodeInformations(create(inject(IntervalCollectionBusiness.class)
				.instanciateOne("ICEV2", "Grading Scale", new String[][]{
						{"A*", "Outstanding", "90", "100"},{"A", "Excellent", "80", "89.99"},{"B", "Very Good", "70", "79.99"},{"C", "Good", "60", "69.99"}
						,{"D", "Satisfactory", "50", "59.99"},{"E", "Fail", "0", "49.99"}})),
				create(inject(IntervalCollectionBusiness.class)
						.instanciateOne("ICP2", "Promotion Scale", new String[][]{
								{"P", "Promoted", "50", "100"},{"PT", "Promoted on trial", "45", "49.99"},{"NP", "Not promoted", "0", "44.99"}})),reportTemplate, "2",interval);	
		CommonNodeInformations commonNodeInformationsG10G12 = commonNodeInformationsG7G9;
		
		JobTitle jobTitle = createEnumeration(JobTitle.class, "Director of studies");
		
		Person signer = new Person();
		signer.setName("O.");
		signer.setLastnames("Olatunji");
		signer.setExtendedInformations(new PersonExtendedInformations(signer));
		signer.getExtendedInformations().setTitle(getEnumeration(PersonTitle.class,PersonTitle.MADAM));
		signer.getExtendedInformations().setSignatureSpecimen(createFile(IesaBusinessLayer.class.getPackage(),"image/signature/o_olatunji.png", "signature_o_olatunji.png"));
		signer.setJobInformations(new JobInformations(signer));
		signer.getJobInformations().setTitle(jobTitle);
		inject(PersonBusiness.class).create(signer);
		
		School school = new School(ownedCompanyBusiness.findDefaultOwnedCompany(),commonNodeInformationsG1G3);
		create(school);
    	
    	school.getOwnedCompany().getCompany().setSigner(signer);
    	companyBusiness.update(school.getOwnedCompany().getCompany());
    	
    	AcademicSession academicSession = new AcademicSession(school,commonNodeInformationsG1G3,new DateTime(2016, 4, 4, 0, 0).toDate());
    	academicSession.setBirthDate(new DateTime(2015, 10, 1, 0, 0).toDate());
    	academicSession.setDeathDate(new DateTime(2016, 6, 1, 0, 0).toDate());
    	academicSession = create(academicSession);
		              
		// Subjects
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_MATHEMATICS_CODE,"Mathematics",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G8});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_GRAMMAR_CODE,"Grammar",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_READING_COMPREHENSION_CODE,"Reading & Comprehension",new ArrayList[]{subjectsG1G3});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_HANDWRITING_CODE,"Hand writing",new ArrayList[]{subjectsG1G3});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_SPELLING_CODE,"Spelling",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_PHONICS_CODE,"Phonics",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_CREATIVE_WRITING_CODE,"Creative writing",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_MORAL_EDUCATION_CODE,"Moral education",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_SOCIAL_STUDIES_CODE,"Social Studies",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G8});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_SCIENCE_CODE,"Science",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_FRENCH_CODE,"French",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ART_CRAFT_CODE,"Art & Craft",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G8});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_MUSIC_CODE,"Music",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G8});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ICT_COMPUTER_CODE,"ICT(Computer)",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_PHYSICAL_EDUCATION_CODE,"Physical education",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G8,subjectsG9G12});
    	
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_LITERATURE_CODE,"Litterature",new ArrayList[]{subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_COMPREHENSION_CODE,"Comprehension",new ArrayList[]{subjectsG4G6});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_HISTORY_CODE,"History",new ArrayList[]{subjectsG4G6,subjectsG7G8,subjectsG9G12});
    	
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ENGLISH_LANGUAGE_CODE,"English Language",new ArrayList[]{subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_LITERATURE_IN_ENGLISH_CODE,"Literature in english",new ArrayList[]{subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_GEOGRAPHY_CODE,"Geography",new ArrayList[]{subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_PHYSICS_CODE,"Physics",new ArrayList[]{subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_CHEMISTRY_CODE,"Chemistry",new ArrayList[]{subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_BIOLOGY_CODE,"Biology",new ArrayList[]{subjectsG7G8,subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_SPANISH_CODE,"Spanish",new ArrayList[]{subjectsG7G8,subjectsG9G12});
    	
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_SOCIOLOGY_CODE,"Sociology",new ArrayList[]{subjectsG9G12});
    	//schoolDataProducerHelper.createOneSubject("Religious studies/Divinity",new ArrayList[]{subjectsG10G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_CORE_MATHEMATICS_CODE,"Core mathematics",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_CHINESE_MANDARIN_CODE,"Chinese Mandarin",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ADVANCED_MATHEMATICS_CODE,"Advanced mathematics",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ACCOUNTING_CODE,"Accounting",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_BUSINESS_STUDIES_CODE,"Business studies",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ECONOMICS_CODE,"Economics",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ART_DESIGN_CODE,"Art & Design",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_DEVELOPMENT_STUDIES_CODE,"Development studies",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_ENVIRONMENTAL_MANAGEMENT_CODE,"Environmental management",new ArrayList[]{subjectsG9G12});
    	schoolDataProducerHelper.createOneSubject(IesaConstant.SUBJECT_LAW_CODE,"Law",new ArrayList[]{subjectsG9G12});
				
    	final Collection<ClassroomSession> classroomSessions = new ArrayList<>(); 
    	final Collection<ClassroomSessionDivision> classroomSessionDivisions = new ArrayList<>(); 
    	final Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects = new ArrayList<>();
    	final List<ClassroomSessionDivisionSubjectEvaluationType> subjectEvaluationTypes = new ArrayList<>();
    	final Collection<ClassroomSessionDivisionStudentsMetricCollection> classroomSessionDivisionStudentsMetricCollections = new ArrayList<>(); 
    	
    	Long gradeIndex = 0l;
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_PK,"Pre-Kindergarten",levelGroupKindergarten,commonNodeInformationsPkg,gradeIndex++) 
    			,null, null,classroomSessionDivisionStudentsMetricCollections,pkMetricCollections,null,Boolean.FALSE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_K1,"Kindergarten 1",levelGroupKindergarten,commonNodeInformationsKg1,gradeIndex++) 
    			, null,null,classroomSessionDivisionStudentsMetricCollections,k1MetricCollections,null,Boolean.FALSE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_K2,"Kindergarten 2",levelGroupKindergarten,commonNodeInformationsKg2Kg3,gradeIndex++) 
    			, null,null,classroomSessionDivisionStudentsMetricCollections,k2k3MetricCollections,null,Boolean.FALSE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_K3,"Kindergarten 3",levelGroupKindergarten,commonNodeInformationsKg2Kg3,gradeIndex++) 
    			, null,null,classroomSessionDivisionStudentsMetricCollections,k2k3MetricCollections,null,Boolean.FALSE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G1,"Grade 1",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}}, subjectsG1G3
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G2,"Grade 2",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}}, subjectsG1G3
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G3,"Grade 3",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG1G3
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G4,"Grade 4",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G5,"Grade 5",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G6,"Grade 6",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,null,Boolean.TRUE,Boolean.TRUE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G7,"Grade 7",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG7G8
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G8,"Grade 8",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG7G8
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G9,"Grade 9",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG9G12
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G10,"Grade 10",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG9G12
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G11,"Grade 11",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG9G12
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(IesaConstant.LEVEL_NAME_CODE_G12,"Grade 12",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG9G12
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.FALSE);
    	
    	classroomSessionBusiness.create(classroomSessions);
    	classroomSessionDivisionBusiness.create(classroomSessionDivisions);
    	classroomSessionDivisionSubjectBusiness.create(classroomSessionDivisionSubjects);
    	subjectEvaluationTypeBusiness.create(subjectEvaluationTypes);
    	classroomSessionDivisionStudentsMetricCollectionBusiness.create(classroomSessionDivisionStudentsMetricCollections);
    	
    	for(AbstractIdentifiable identifiable :  genericDao.use(EvaluationType.class).select().all())
    		SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.getEvaluationTypeCodes().add(((EvaluationType)identifiable).getCode());
    	SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.setSumMarks(Boolean.TRUE);
    	
    	/* reading data from excel files */
	}
	
	@Override
	protected void persistSecurityData(){
		UniformResourceLocatorBusiness uniformResourceLocatorBusiness = inject(UniformResourceLocatorBusiness.class);
		Role userRole = getEnumeration(Role.class,Role.USER);
		Role managerRole = getEnumeration(Role.class,Role.MANAGER);
		Role teacherRole = create(new Role(IesaConstant.ROLE_TEACHER_CODE, "Teacher"));
		
		instanciateRoleUniformResourceLocator(userRole, uniformResourceLocatorBusiness.instanciateOne("/private/index.jsf",new String[]{})
				,uniformResourceLocatorBusiness.instanciateOne("/private/file/consultmany.jsf",new String[]{})
				,uniformResourceLocatorBusiness.instanciateOne("/private/classroomsessiondivision/consult.jsf",new String[]{UniformResourceLocatorParameter.TAB_ID,"school.broadsheet" }));
		
		instanciateRoleUniformResourceLocator(managerRole,uniformResourceLocatorBusiness.instanciateManyCrud(Student.class)
				,uniformResourceLocatorBusiness.instanciateManyCrud(Teacher.class)
				,uniformResourceLocatorBusiness.instanciateManyCrud(Employee.class)
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(ClassroomSession.class, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,null, null, new String[]{"auscsdrf","acscsdrf"})
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(ClassroomSessionDivision.class, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,null, null, null)
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(ClassroomSessionDivisionSubject.class, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,null, null, null)
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(ClassroomSessionDivisionSubjectEvaluationType.class, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,null, new String[]{"acse","auscsdr"}, null)
				);
		
		instanciateRoleUniformResourceLocator(teacherRole
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(StudentClassroomSession.class, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,Boolean.TRUE, null, null)
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(StudentClassroomSessionDivision.class, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,null, null, null)
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(StudentClassroomSessionDivisionSubject.class, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,Boolean.TRUE, null, null)
				,uniformResourceLocatorBusiness.instanciateManyBusinessCrud(Evaluation.class, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,null, null, null));
		
		//UniformResourceLocator classroomSessionDivisionUpdateStudentResults = new UniformResourceLocator("/private/classroomsessiondivision/updatestudentresults.jsf");
		//UniformResourceLocator classroomSessionDivisionUpdateStudentReport = new UniformResourceLocator("/private/classroomsessiondivision/updatestudentreport.jsf");
		
		instanciateUserAccountsFromActors(teacherDao.readAll(), userRole,teacherRole);
		
		/*inject(UniformResourceLocatorBusiness.class).create(rootDataProducerHelper.getUniformResourceLocators());
		inject(RoleUniformResourceLocatorBusiness.class).create(rootDataProducerHelper.getRoleUniformResourceLocators());
		inject(UserAccountBusiness.class).create(rootDataProducerHelper.getUserAccounts());
		*/
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
	
	private void createMetricCollections(){
		String[][] valueIntervals = new String[][]{ {"1", "Learning to do", "1", "1"},{"2", "Does sometimes", "2", "2"} ,{"3", "Does regularly", "3", "3"} };
		pkMetricCollections = new MetricCollection[]{ create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_EXPRESSIVE_LANGUAGE,"Expressive language",MetricValueType.NUMBER
    			, new String[]{"Participates actively during circle time","Participates in singing rhymes","Can say her name and name of classmates"
    			,"Can respond appropriately to “how are you?”","Can say his/her age","Can say the name of her school","Names objects in the classroom and school environment"
    			,"Uses at least one of the following words “me”,“I”, “he”, “she”, “you”","Talks in two or three word phrases and longer sentences"
    			,"Can use “and” to connect words/phrases","Talks with words in correct order","Can be engaged in conversations"}
    	,"Skills Performance levels", valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_RECEPTIVE_LANGUAGE,"Receptive language",MetricValueType.NUMBER
    			, new String[]{"Responds to her name when called",
    			"Retrieves named objects",
    			"Follows simple instructions (across the classroom) – stand, sit, bring your cup",
    			"Understands facial expressions and tone of voice",
    			"Understands 2-3 step instructions",
    			"Understands positional words – In and out - Up and down - On and under - Forward and backward",
    			"Understands the concept “Give and Take”",
    			"Talks about feelings"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_READING_READNESS,"Reading readness",MetricValueType.NUMBER
    			, new String[]{"Shows interest in books/stories",
    			"Names familiar objects in pictures/books – vegetables, fruits, animals",
    			"Tells what action is going on in pictures",
    			"Handling books – carrying a book, turning the pages of a book, placing a book back in the shelf",
    			"Listening for different sounds in the environment",
    			"Identifying objects that begin with a particular sound",
    			"Identifying pictures that begin with a particular sound",
    			"Recognizes the written letters of the alphabet"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_NUMERACY_DEVELOPMENT,"Numeracy development",MetricValueType.NUMBER
    			, new String[]{"Sorts objects by shape",
    			"Sorts objects by size",
    			"Participates in reciting different counting rhymes, songs, stories and games",
    			"Verbally count forward to 10",
    			"Can count 1-10 objects",
    			"Identifies the written numerals 1-10",
    			"Reproducing Patterns",
    			"Identifies the 3 basic geometric shapes ( circle, triangle and square)",
    			"Identifies more shapes ( Star, diamond, heart, cross ,crescent)"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_ARTS_MUSIC,"Arts and music",MetricValueType.NUMBER
    			, new String[]{"Moves expressively to sounds and music – nodding, clapping, movement of body",
    			"Participates in musical activities",
    			"Hums or sing words of songs",
    			"Participates in role play",
    			"Shows satisfaction with completed work"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_SOCIAL_EMOTIONAL_DEVELOPMENT,"Social and emotional development",MetricValueType.NUMBER
    			, new String[]{"Initiates interaction with adults",
    			"Initiates interaction with classmates",
    			"Participates in group activities",
    			"Takes turns during group activities",
    			"Greets people – hello and goodbye",
    			"Says “please” and “thank you”",
    			"Asks for help in doing things when needed",
    			"Shows sympathy, offers to help or helps others",
    			"Can express dissatisfaction and other emotions – body language or words",
    			"Responds to correction – stops the misbehaviour"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_GROSS_MOTOR_SKILLS,"Gross motor skills",MetricValueType.NUMBER
    			, new String[]{"Can run well without falling",
    			"Can kick a ball",
    			"Climbs up ladder and slides down slide without help",
    			"Walks up and down stairs unassisted",
    			"Can stand on one foot for a few seconds without support",
    			"Throws a ball into a basket from a short distance"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_PK_STUDENT_FINE_MOTOR_SKILLS,"Fine motor skills",MetricValueType.NUMBER
    			, new String[]{"Scribbles spontaneously",
    			"Can scribble to and from, in circular motions and in lines",
    			"Can place simple pieces in a puzzle board",
    			"Can build a tower of at least 3-5 blocks",
    			"Develops good pencil grip and control"}
    	, valueIntervals))
    	};		
    	
		valueIntervals = new String[][]{ {"1", "Emerging", "1", "1"}
    	,{"2", "Developing", "2", "2"} 
    	,{"3", "Proficient", "3", "3"},{"4", "Exemplary", "4", "4"} };
		k1MetricCollections = new MetricCollection[]{ create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K1_STUDENT_ENGLISH_LANGUAGE_ARTS_READING,"English/language/Arts/Reading",MetricValueType.NUMBER
    			, new String[]{"Reads independently with understanding","Comprehends a variety of texts","Applies a variety of strategies to comprehend printed text"
    					,"Reads to access and utilize information from written and electronic sources","Demonstrates understanding of letter-sound associations"}
    	,"Skills Performance levels", valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K1_STUDENT_COMMUNICATION_SKILLS,"Communication skills",MetricValueType.NUMBER
    			, new String[]{"Contributes ideas to discussions","Communicates ideas effectively","Write for a variety of purposes","Writes well-organized compositions"
    					,"Uses appropriate writing skills","Write legibly","Revises, edits and proofreads work"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K1_STUDENT_SCIENCE,"Science",MetricValueType.NUMBER
    			, new String[]{"Understands and applies scientific process","Understands and applies knowledge of key concepts"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K1_STUDENT_SOCIAL_STUDIES,"Social Studies",MetricValueType.NUMBER
    			, new String[]{"Gathers and organizes information","Understands and applies knowledge of key concepts"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K1_STUDENT_MATHEMATICS,"Mathematics",MetricValueType.NUMBER
    			, new String[]{"Demonstrates understanding of number sense","Reads and interprets data","Applies problem-solving strategies","Communicates mathematically"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K1_STUDENT_WORK_HABITS,"Work habits",MetricValueType.NUMBER
    			, new String[]{"Follows directions","Uses time and materials constructively ","Works independently","Completes class assignments","Completes homework assignments",
    			"Listens attentively"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K1_STUDENT_SOCIAL_SKILLS,"Social Skills",MetricValueType.NUMBER
    			, new String[]{"Cooperates with others","Shows respect for others","Participates in classroom activities","Follows classroom/school rules"}
    	, valueIntervals))
    	};
		
		valueIntervals = new String[][]{ {"1", "Does not meets and applies expectations/standards; shows no growth even with support", "1", "1"}
    	,{"2", "Does not meets and applies expectations/standards; but shows growth with support", "2", "2"} 
    	,{"3", "Meets and applies expectations/standards with support", "3", "3"},{"4", "Meets and applies expectations/standards with support", "4", "4"} };
    	
		k2k3MetricCollections = new MetricCollection[]{ create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_READING_READINESS,"Reading Readiness",MetricValueType.NUMBER
    			, new String[]{"Demonstrates concepts of print","Identifies and produces rhyming words","Segments and blends sounds"}
    	,"Performance Codes", valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_READING,"Reading",MetricValueType.NUMBER
    			, new String[]{"Answers questions about essential narrative elements","Reads high frequency words","Blends sounds to read words","Reads simple text"
    					,"Developmental Reading assessment"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_WRITING,"Writing",MetricValueType.NUMBER
    			, new String[]{"Writes first and last name","Expresses ideas through independent writing"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_LISTENING_SPEAKING_VIEWING,"Listening,Speaking and Viewing",MetricValueType.NUMBER
    			, new String[]{"Uses oral language to communicate effectively","Recites short poems and songs","Follows two-step oral directions"
    					,"Makes predictions and retells","Comprehends information through listening","Demonstrates comprehension of information through speaking"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_ALPHABET_IDENTIFICATION,"Alphabet identification",MetricValueType.NUMBER
    			, new String[]{"Identifies Upper-Case","Identifies Lower-Case","Produces Letter Sounds","Prints Letters Correctly"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_MATHEMATICS,"Mathematics",MetricValueType.NUMBER
    			, new String[]{"Number and Operations","Geometry","Measurement","Algebraic Thinking"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_SCIENCE_SOCIAL_STUDIES_MORAL_EDUCATION,"Science, Social Studies and Moral Education",MetricValueType.NUMBER
    			, new String[]{"Science","Social Studies","Moral Education"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_ART_CRAFT,"Art and Craft",MetricValueType.NUMBER
    			, new String[]{"Performance","Initiative"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_MUSIC,"Music",MetricValueType.NUMBER
    			, new String[]{"Performance","Initiative"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_PHYSICAL_EDUCATION,"Physical Education",MetricValueType.NUMBER
    			, new String[]{"Performance","Initiative"}
    	, valueIntervals))
    	,create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_K2_K3_STUDENT_WORK_BEHAVIOUR_HABITS,"Work and Behaviour Habits",MetricValueType.NUMBER
    			, new String[]{"Follows directions","Uses time and materials constructively","Works independently","Completes class assignments"
    					,"Completes homework assignments","Listens attentively","Cooperates with others","Shows respect for others","Participates in classroom activities"
    					,"Follows classroom/school rules"}
    	, valueIntervals))
    	};
		
		g1g6MetricCollections = new MetricCollection[]{ create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_G1_G6_STUDENT_BEHAVIOUR,"Behaviour,Study and Work Habits",MetricValueType.NUMBER
    			, new String[]{"Respect authority","Works independently and neatly","Completes homework and class work on time","Shows social courtesies","Demonstrates self-control"
    					,"Takes care of school and others materials","Game/Sport","Handwriting","Drawing/Painting","Punctionality/Regularity","Works cooperatively in groups"
    					,"Listens and follows directions"}
    	,"Effort Levels", new String[][]{ {"1", "Has no regard for the observable traits", "1", "1"},{"2", "Shows minimal regard for the observable traits", "2", "2"}
    	,{"3", "Acceptable level of observable traits", "3", "3"},{"4", "Maintains high level of observable traits", "4", "4"}
    	,{"5", "Maintains an excellent degree of observable traits", "5", "5"} }))};
   
		g7g12MetricCollections = new MetricCollection[]{ create(inject(MetricCollectionBusiness.class).instanciateOne(IesaConstant.MERIC_COLLECTION_G7_G12_STUDENT_BEHAVIOUR,"Behaviour,Study and Work Habits",MetricValueType.STRING
    			, new String[]{"Respect authority","Works independently and neatly","Completes homework and class work on time","Shows social courtesies","Demonstrates self-control"
    					,"Takes care of school and others materials","Game/Sport","Handwriting","Drawing/Painting","Punctionality/Regularity","Works cooperatively in groups"
    					,"Listens and follows directions"}
    	,"Effort Levels", new String[][]{ {"E", "Excellent", "1", "1"},{"G", "Good", "2", "2"},{"S", "Satisfactory", "3", "3"},{"N", "Needs Improvement", "4", "4"}
    	,{"H", "Has no regard", "5", "5"} }).setMetricValueInputted(MetricValueInputted.VALUE_INTERVAL_CODE))};
	}
	
	private CommonNodeInformations instanciateCommonNodeInformations(IntervalCollection intervalCollection,IntervalCollection promotionIntervalCollection
			,ReportTemplate reportTemplate,String classroomsessionDivisionIndex,Interval interval){
		CommonNodeInformations commonNodeInformations = schoolDataProducerHelper.instanciateOneCommonNodeInformations(intervalCollection,promotionIntervalCollection, reportTemplate
				, TimeDivisionType.DAY, TimeDivisionType.TRIMESTER,"50", classroomsessionDivisionIndex,interval);
		return commonNodeInformations;
	}
	
	private CommonNodeInformations instanciateCommonNodeInformations(IntervalCollection intervalCollection,IntervalCollection promotionIntervalCollection
			,File reportHeaderFile,File backgroundFile,String studentClassroomsessionDivisionReportTemplateFileName,String classroomsessionDivisionIndex,Interval interval){
		//String fileName = "studentclassroomsessiondivisionreport_"+studentClassroomsessionDivisionReportTemplateFileName;
		//File reportFile = createFile("report/studentclassroomsessiondivision/"+studentClassroomsessionDivisionReportTemplateFileName, fileName);
		
		ReportTemplate reportTemplate = createReportTemplate(studentClassroomsessionDivisionReportTemplateFileName,"student classroomsession division report",Boolean.TRUE
    			, "report/studentclassroomsessiondivision/"+studentClassroomsessionDivisionReportTemplateFileName, null, null, null);
		
		//ReportTemplate reportTemplate = new ReportTemplate(studentClassroomsessionDivisionReportTemplateFileName,reportFile,reportHeaderFile,backgroundFile,null);
		return instanciateCommonNodeInformations(intervalCollection,promotionIntervalCollection,create(reportTemplate),classroomsessionDivisionIndex,interval);
	}
	
	@Override
	public void installApplication(Boolean fake) {
		ApplicationBusinessImpl.Listener.COLLECTION.add(new ApplicationBusinessImpl.Listener.Adapter.Default(){
			private static final long serialVersionUID = -7737204312141333272L;
    		@Override
    		public void installationStarted(Installation installation) {
    			installation.getApplication().setUniformResourceLocatorFilteringEnabled(Boolean.TRUE);
    			installation.getApplication().setWebContext("iesaschool");
    			installation.getApplication().setName("IESA Management System");
    			super.installationStarted(installation);
    		}
    	});
		super.installApplication(fake);
	}
	
	public StudentClassroomSession.SearchCriteria getStudentClassroomSessionSearchCriteria(){
		StudentClassroomSession.SearchCriteria searchCriteria = new StudentClassroomSession.SearchCriteria();
		searchCriteria.getDivisionCount().setLowest(2);
		searchCriteria.getDivisionCount().setHighest(3);
		searchCriteria.getDivisionIndexesRequired().add(0);
		searchCriteria.getDivisionIndexesRequired().add(1);
		searchCriteria.getDivisionIndexesRequired().add(2);
		return searchCriteria;
	}
}
