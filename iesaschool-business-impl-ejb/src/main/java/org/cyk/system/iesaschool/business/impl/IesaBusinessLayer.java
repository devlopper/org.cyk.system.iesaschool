package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.CompanyBusinessLayerAdapter;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.BusinessLayer;
import org.cyk.system.root.business.api.BusinessLayerListener;
import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootDataProducerHelper.RootDataProducerHelperListener;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.mathematics.IntervalCollection;
import org.cyk.system.root.model.mathematics.MetricCollection;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.session.SchoolReportProducer;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.business.api.subject.SubjectEvaluationTypeBusiness;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.SchoolBusinessTestHelper.ClassroomSessionDivisionInfos;
import org.cyk.system.school.business.impl.SchoolBusinessTestHelper.ClassroomSessionDivisionSubjectInfos;
import org.cyk.system.school.business.impl.SchoolBusinessTestHelper.ClassroomSessionInfos;
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
import org.cyk.system.school.model.subject.EvaluationType;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.system.school.model.subject.SubjectEvaluationType;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.joda.time.DateTimeConstants;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=IesaBusinessLayer.DEPLOYMENT_ORDER) @Getter
public class IesaBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = SchoolBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static IesaBusinessLayer INSTANCE;
	
	@Inject private RootBusinessLayer rootBusinessLayer;
	@Inject private RootRandomDataProvider rootRandomDataProvider;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private SchoolBusinessLayer schoolBusinessLayer;
	
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject private CompanyBusiness companyBusiness;
	@Inject private SubjectEvaluationTypeBusiness subjectEvaluationTypeBusiness;
	@Inject private ClassroomSessionBusiness classroomSessionBusiness;
	@Inject private ClassroomSessionDivisionBusiness classroomSessionDivisionBusiness;
	@Inject private ClassroomSessionDivisionSubjectBusiness classroomSessionDivisionSubjectBusiness;
	
	private EvaluationType evaluationTypeTest1,evaluationTypeTest2,evaluationTypeExam;
	
	private MetricCollection studentWorkMetricCollection;
	
	private ArrayList<Subject> subjectsG1G3 = new ArrayList<>(),subjectsG4G6 = new ArrayList<>()
			,subjectsG7G9 = new ArrayList<>(),subjectsG10G12 = new ArrayList<>(); 
	private LevelGroupType levelGroupType;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		rootBusinessLayer.getBusinessLayerListeners().add(new BusinessLayerListener.BusinessLayerListenerAdapter(){
			private static final long serialVersionUID = -9212697312172717454L;
			@Override
			public void beforeInstall(BusinessLayer businessLayer,Installation installation) {
				installation.getApplication().setName("IESA WebApp");
				super.beforeInstall(businessLayer, installation);
			}
		});
		
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
		levelGroupType = createEnumeration(LevelGroupType.class,"LevelGroupTypeDummy");
		LevelGroup levelGroupPrimary = createEnumeration(LevelGroup.class,"Primary");
		LevelGroup levelGroupSecondary = createEnumeration(LevelGroup.class, "Secondary");
		//LevelGroup levelGroupPrimary = new LevelGroup(null, null, "PRIMARY", "Primary");
		
    	evaluationTypeTest1 = createEnumeration(EvaluationType.class,IesaConstant.EVALUATION_TYPE_TEST1,"Test 1");
    	evaluationTypeTest2 = createEnumeration(EvaluationType.class,IesaConstant.EVALUATION_TYPE_TEST2,"Test 2");
    	evaluationTypeExam = createEnumeration(EvaluationType.class,IesaConstant.EVALUATION_TYPE_EXAM,"Exam");
    	
		studentWorkMetricCollection = new MetricCollection("BSWH","Behaviour,Study and Work Habits");
		studentWorkMetricCollection.addItem("1","Respect authority");
		studentWorkMetricCollection.addItem("2","Works independently and neatly");
		studentWorkMetricCollection.addItem("3","Completes homework and class work on time");
		studentWorkMetricCollection.addItem("4","Shows social courtesies");
		studentWorkMetricCollection.addItem("5","Demonstrates self-control");
		studentWorkMetricCollection.addItem("6","Takes care of school and others materials");
		studentWorkMetricCollection.addItem("7","Game/Sport");
		studentWorkMetricCollection.addItem("8","Handwriting");
		studentWorkMetricCollection.addItem("9","Drawing/Painting");
		studentWorkMetricCollection.addItem("10","Punctionality/Regularity");
		studentWorkMetricCollection.addItem("11","Works cooperatively in groups");
		studentWorkMetricCollection.addItem("12","Listens and follows directions");
		
		studentWorkMetricCollection.setValueIntervalCollection(new IntervalCollection("BSWH_METRIC_IC"));
		studentWorkMetricCollection.getValueIntervalCollection().addItem("1", "Has no regard for the observable traits", "1", "1");
		studentWorkMetricCollection.getValueIntervalCollection().addItem("2", "Shows minimal regard for the observable traits", "2", "2");
		studentWorkMetricCollection.getValueIntervalCollection().addItem("3", "Acceptable level of observable traits", "3", "3");
		studentWorkMetricCollection.getValueIntervalCollection().addItem("4", "Maintains high level of observable traits", "4", "4");
		studentWorkMetricCollection.getValueIntervalCollection().addItem("5", "Maintains an excellent degree of observable traits", "5", "5");
		
		create(studentWorkMetricCollection);
		
		File reportFile = createFile("report/studentclassroomsessiondivision.jrxml", "studentclassroomsessiondivisionreport.jrxml");
		ReportTemplate reportTemplate = new ReportTemplate("SCSDRT",reportFile,createFile("image/studentclassroomsessiondivisionreport_background.jpg"));
		create(reportTemplate);
		
		CommonNodeInformations commonNodeInformationsG1G3 = new CommonNodeInformations(createIntervalCollection("ICEV1",new String[][]{
			{"A+", "Excellent", "90", "100"},{"A", "Very good", "80", "89.99"},{"B+", "Good", "70", "79.99"},{"B", "Fair", "60", "69.99"}
			,{"C+", "Satisfactory", "55", "59.99"},{"C", "Barely satisfactory", "50", "54.99"},{"E", "Fail", "0", "49.99"}},Constant.CHARACTER_SLASH.toString())
				,studentWorkMetricCollection,reportTemplate,getEnumeration(TimeDivisionType.class, TimeDivisionType.DAY));
		
		CommonNodeInformations commonNodeInformationsG4G6 = commonNodeInformationsG1G3;
		
		CommonNodeInformations commonNodeInformationsG7G9 = new CommonNodeInformations(createIntervalCollection("ICEV2",new String[][]{
			{"A*", "Outstanding", "90", "100"},{"A", "Excellent", "80", "89.99"},{"B", "Very Good", "70", "79.99"},{"C", "Good", "60", "69.99"}
			,{"D", "Satisfactory", "50", "59.99"},{"E", "Fail", "0", "49.99"}},Constant.CHARACTER_SLASH.toString()),studentWorkMetricCollection,reportTemplate
				,getEnumeration(TimeDivisionType.class, TimeDivisionType.DAY));
		
		CommonNodeInformations commonNodeInformationsG10G12 = commonNodeInformationsG7G9;
		
		School school = new School(ownedCompanyBusiness.findDefaultOwnedCompany(),null);
    	create(school);
    	
    	school.getOwnedCompany().getCompany().setManager(rootRandomDataProvider.oneFromDatabase(Person.class));
    	companyBusiness.update(school.getOwnedCompany().getCompany());
    	
    	AcademicSession academicSession = new AcademicSession(school,null,new Date());
    	academicSession.getPeriod().setFromDate(new Date());
    	academicSession.getPeriod().setToDate(new Date(academicSession.getPeriod().getFromDate().getTime()+DateTimeConstants.MILLIS_PER_DAY*355));
    	academicSession = create(academicSession);
		
		// Subjects
    	createSubject("Mathematics",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	createSubject("Grammar",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	createSubject("Reading & Comprehension",new ArrayList[]{subjectsG1G3});
    	createSubject("Hand writing",new ArrayList[]{subjectsG1G3});
    	createSubject("Spelling",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	createSubject("Phonics",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	createSubject("Creative writing",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	createSubject("Moral education",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	createSubject("Social Studies",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	createSubject("Science",new ArrayList[]{subjectsG1G3,subjectsG4G6});
    	createSubject("French",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	createSubject("Art & Craft",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	createSubject("Music",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	createSubject("ICT",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	createSubject("Physical education",new ArrayList[]{subjectsG1G3,subjectsG4G6,subjectsG7G9});
    	
    	createSubject("Litterature",new ArrayList[]{subjectsG4G6});
    	createSubject("Comprehension",new ArrayList[]{subjectsG4G6});
    	createSubject("History",new ArrayList[]{subjectsG4G6,subjectsG7G9,subjectsG10G12});
    	
    	createSubject("English Language",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	createSubject("Literature in english",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	createSubject("Geography",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	createSubject("Physics",new ArrayList[]{subjectsG7G9,subjectsG10G12});
    	createSubject("Chemistry",new ArrayList[]{subjectsG7G9});
    	createSubject("Biology",new ArrayList[]{subjectsG7G9});
    	createSubject("Spanish",new ArrayList[]{subjectsG7G9});
    	
    	createSubject("Sociology",new ArrayList[]{subjectsG10G12});
    	createSubject("Religious studies/Divinity",new ArrayList[]{subjectsG10G12});
    	createSubject("Core mathematics",new ArrayList[]{subjectsG10G12});
    	createSubject("Advanced mathematics",new ArrayList[]{subjectsG10G12});
				
    	Collection<ClassroomSession> classroomSessions = new ArrayList<>(); 
    	Collection<ClassroomSessionDivision> classroomSessionDivisions = new ArrayList<>(); 
    	Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects = new ArrayList<>();
    	Collection<SubjectEvaluationType> subjectEvaluationTypes = new ArrayList<>(); 
    	
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 1",levelGroupPrimary,commonNodeInformationsG1G3) , subjectsG1G3,new String[]{"A","B","C"});    	
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 2",levelGroupPrimary,commonNodeInformationsG1G3) , subjectsG1G3,new String[]{"A","B","C"});
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 3",levelGroupPrimary,commonNodeInformationsG1G3) , subjectsG1G3,new String[]{"A","B","C"});
    	
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 4",levelGroupPrimary,commonNodeInformationsG4G6) , subjectsG4G6,new String[]{"A","B","C"});
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 5",levelGroupPrimary,commonNodeInformationsG4G6) , subjectsG4G6,new String[]{"A","B","C"});
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 6",levelGroupPrimary,commonNodeInformationsG4G6) , subjectsG4G6,new String[]{"A","B","C"});
    	
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 7",levelGroupSecondary,commonNodeInformationsG7G9) , subjectsG7G9,new String[]{"A","B","C"});
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 8",levelGroupSecondary,commonNodeInformationsG7G9) , subjectsG7G9,new String[]{"A","B","C"});
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 9",levelGroupSecondary,commonNodeInformationsG7G9) , subjectsG7G9,new String[]{"A","B","C"});
    	
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 10",levelGroupSecondary,commonNodeInformationsG10G12) , subjectsG10G12,new String[]{"A","B","C"});
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 11",levelGroupSecondary,commonNodeInformationsG10G12) , subjectsG10G12,new String[]{"A","B","C"});
    	grade(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, createLevelTimeDivision("Grade 12",levelGroupSecondary,commonNodeInformationsG10G12) , subjectsG10G12,new String[]{"A","B","C"});
    	
    	classroomSessionBusiness.create(classroomSessions);
    	classroomSessionDivisionBusiness.create(classroomSessionDivisions);
    	classroomSessionDivisionSubjectBusiness.create(classroomSessionDivisionSubjects);
    	subjectEvaluationTypeBusiness.create(subjectEvaluationTypes);
    	
    	for(AbstractIdentifiable identifiable : genericBusiness.use(EvaluationType.class).find().all())
    		SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.getEvaluationTypeCodes().add(((EvaluationType)identifiable).getCode());
    	SchoolReportProducer.DEFAULT_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT_PARAMETERS.setSumMarks(Boolean.TRUE);
	}
	
	private LevelTimeDivision createLevelTimeDivision(String levelName,LevelGroup levelGroup,CommonNodeInformations commonNodeInformations){
		commonNodeInformations.setAggregateAttendance(Boolean.FALSE);
		LevelName _levelName = createEnumeration(LevelName.class,levelName);
		_levelName.setNodeInformations(commonNodeInformations);
		return create(new LevelTimeDivision(create(new Level(levelGroup,_levelName)), getEnumeration(TimeDivisionType.class,TimeDivisionType.YEAR)));
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
	
	private Subject createSubject(String name,ArrayList<Subject>[] collections){
		Subject subject = createEnumeration(Subject.class,name);
		for(Collection<Subject> collection : collections)
			collection.add(subject);
		return subject;
	}
	
	private void grade(Collection<ClassroomSession> classroomSessions,Collection<ClassroomSessionDivision> classroomSessionDivisions
			,Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects,Collection<SubjectEvaluationType> subjectEvaluationTypes
			,AcademicSession academicSession,LevelTimeDivision levelTimeDivision,Collection<Subject> subjects,String[] suffixes){
		for(String suffix : suffixes){
			ClassroomSession classroomSession = new ClassroomSession(academicSession, levelTimeDivision,null);
			classroomSession.setSuffix(StringUtils.isBlank(suffix)?null:suffix);
			classroomSession.getPeriod().setFromDate(new Date());
			classroomSession.getPeriod().setToDate(new Date());
			classroomSessions.add(classroomSession);
			ClassroomSessionInfos classroomSessionInfos = new ClassroomSessionInfos(classroomSession);
			classroomSessionInfos.getDivisions().add(createClassroomSessionDivision(classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,classroomSessionInfos.getClassroomSession(),subjects));
			classroomSessionInfos.getDivisions().add(createClassroomSessionDivision(classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,classroomSessionInfos.getClassroomSession(),subjects));
			classroomSessionInfos.getDivisions().add(createClassroomSessionDivision(classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,classroomSessionInfos.getClassroomSession(),subjects));
		}
	}
	/*
	private void grade(Collection<ClassroomSession> classroomSessions,Collection<ClassroomSessionDivision> classroomSessionDivisions
			,Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects,Collection<SubjectEvaluationType> subjectEvaluationTypes
			,AcademicSession academicSession,LevelTimeDivision levelTimeDivision,Collection<Subject> subjects){
		grade(classroomSessions, classroomSessionDivisions, classroomSessionDivisionSubjects, subjectEvaluationTypes, academicSession, levelTimeDivision, subjects,new String[]{""});
	}*/
	
	private ClassroomSessionDivisionInfos createClassroomSessionDivision(Collection<ClassroomSessionDivision> classroomSessionDivisions
			,Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects,Collection<SubjectEvaluationType> subjectEvaluationTypes
			,ClassroomSession classroomSession,Collection<Subject> subjects){
		ClassroomSessionDivision classroomSessionDivision = new ClassroomSessionDivision(classroomSession,getEnumeration(TimeDivisionType.class,TimeDivisionType.TRIMESTER)
    			,new BigDecimal("1"));
		classroomSessionDivision.setDuration(DateTimeConstants.MILLIS_PER_DAY * 45l);
		classroomSessionDivisions.add(classroomSessionDivision);
		classroomSessionDivision.getPeriod().setFromDate(new Date());
		classroomSessionDivision.getPeriod().setToDate(new Date());
		ClassroomSessionDivisionInfos classroomSessionDivisionInfos = new ClassroomSessionDivisionInfos(classroomSessionDivision);
		
		for(Subject subject : subjects){
			classroomSessionDivisionInfos.getSubjects().add(createClassroomSessionDivisionSubject(classroomSessionDivisionSubjects,subjectEvaluationTypes,classroomSessionDivision,subject,new Object[][]{
				{evaluationTypeTest1,"0.15"},{evaluationTypeTest2,"0.15"},{evaluationTypeExam,"0.7"}
			}));
    	}
    	
		return classroomSessionDivisionInfos;
	}
	
	private ClassroomSessionDivisionSubjectInfos createClassroomSessionDivisionSubject(Collection<ClassroomSessionDivisionSubject> classroomSessionDivisionSubjects,Collection<SubjectEvaluationType> subjectEvaluationTypes,ClassroomSessionDivision classroomSessionDivision,Subject subject,Object[][] evaluationTypes){
		ClassroomSessionDivisionSubject classroomSessionDivisionSubject = new ClassroomSessionDivisionSubject(classroomSessionDivision,subject,BigDecimal.ONE,null);
		classroomSessionDivisionSubjects.add(classroomSessionDivisionSubject);
		ClassroomSessionDivisionSubjectInfos classroomSessionDivisionSubjectInfos = new ClassroomSessionDivisionSubjectInfos(classroomSessionDivisionSubject);
		for(Object[] evaluationType : evaluationTypes){
			Object[] infos = evaluationType;
			classroomSessionDivisionSubjectInfos.getEvaluationTypes().add(createSubjectEvaluationType(subjectEvaluationTypes,classroomSessionDivisionSubject, (EvaluationType)infos[0], new BigDecimal((String)infos[1])));
		}
		return classroomSessionDivisionSubjectInfos;
	}
	
	private SubjectEvaluationType createSubjectEvaluationType(Collection<SubjectEvaluationType> subjectEvaluationTypes,ClassroomSessionDivisionSubject subject,EvaluationType name,BigDecimal coefficient){
		SubjectEvaluationType subjectEvaluationType = new SubjectEvaluationType(subject,name,coefficient,new BigDecimal("100"));
		subjectEvaluationTypes.add(subjectEvaluationType);
		return subjectEvaluationType;
	}
    
}
