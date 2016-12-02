package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.structure.Company;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.mathematics.IntervalBusiness;
import org.cyk.system.root.business.api.mathematics.IntervalCollectionBusiness;
import org.cyk.system.root.business.api.mathematics.MetricCollectionIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness.FormatArguments;
import org.cyk.system.root.business.api.party.person.PersonBusiness;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.AbstractIdentifiableBusinessServiceImpl;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.file.FileIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.file.report.ReportTemplate;
import org.cyk.system.root.model.globalidentification.GlobalIdentifier;
import org.cyk.system.root.model.mathematics.Interval;
import org.cyk.system.root.model.mathematics.IntervalCollection;
import org.cyk.system.root.model.mathematics.MetricCollection;
import org.cyk.system.root.model.mathematics.MetricCollectionIdentifiableGlobalIdentifier;
import org.cyk.system.root.model.message.SmtpProperties;
import org.cyk.system.root.model.party.person.JobInformations;
import org.cyk.system.root.model.party.person.JobTitle;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.party.person.PersonExtendedInformations;
import org.cyk.system.root.model.party.person.PersonTitle;
import org.cyk.system.root.model.party.person.Sex;
import org.cyk.system.root.model.security.Credentials;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.persistence.api.GenericDao;
import org.cyk.system.root.persistence.api.file.FileDao;
import org.cyk.system.school.business.api.session.AcademicSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.session.LevelGroupBusiness;
import org.cyk.system.school.business.api.session.LevelGroupTypeBusiness;
import org.cyk.system.school.business.api.session.StudentClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectEvaluationTypeBusiness;
import org.cyk.system.school.business.api.subject.EvaluationTypeBusiness;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.SchoolDataProducerHelper;
import org.cyk.system.school.business.impl.actor.StudentBusinessImpl;
import org.cyk.system.school.business.impl.actor.TeacherBusinessImpl;
import org.cyk.system.school.business.impl.subject.ClassroomSessionDivisionSubjectBusinessImpl;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.actor.Teacher;
import org.cyk.system.school.model.session.AcademicSession;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.ClassroomSessionDivision;
import org.cyk.system.school.model.session.CommonNodeInformations;
import org.cyk.system.school.model.session.Level;
import org.cyk.system.school.model.session.LevelGroup;
import org.cyk.system.school.model.session.LevelGroupType;
import org.cyk.system.school.model.session.LevelTimeDivision;
import org.cyk.system.school.model.session.School;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.session.StudentClassroomSessionDivision;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.ClassroomSessionDivisionSubjectEvaluationType;
import org.cyk.system.school.model.subject.EvaluationType;
import org.cyk.system.school.model.subject.StudentClassroomSessionDivisionSubject;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.system.school.persistence.api.actor.StudentDao;
import org.cyk.system.school.persistence.api.actor.TeacherDao;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.joda.time.DateTime;

import lombok.Getter;

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
		PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
				/*if(SchoolConstant.REPORT_STUDENT_CLASSROOM_SESSION_DIVISION_SHEET.equals(instanceCode)){
					if(PersistDataListener.BASE_PACKAGE.equals(name))
						return (T) IesaBusinessLayer.class.getPackage();
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "report/studentclassroomsessiondivision/g1g12.jrxml";
				}*/
				
				if(File.class.equals(aClass)){
					if(CompanyConstant.FILE_DOCUMENT_HEADER.equals(instanceCode)){
						if(PersistDataListener.BASE_PACKAGE.equals(name))
							return (T) IesaBusinessLayer.class.getPackage();
						if(PersistDataListener.RELATIVE_PATH.equals(name))
							return (T) "image/document_header.png";
					}else if(CompanyConstant.FILE_DOCUMENT_BACKGROUND.equals(instanceCode)){
						if(PersistDataListener.BASE_PACKAGE.equals(name))
							return (T) IesaBusinessLayer.class.getPackage();
						if(PersistDataListener.RELATIVE_PATH.equals(name))
							return (T) "image/studentclassroomsessiondivisionreport_background.jpg";
					}else if(CompanyConstant.FILE_DOCUMENT_BACKGROUND_DRAFT.equals(instanceCode)){
						if(PersistDataListener.BASE_PACKAGE.equals(name))
							return (T) IesaBusinessLayer.class.getPackage();
						if(PersistDataListener.RELATIVE_PATH.equals(name))
							return (T) "image/studentclassroomsessiondivisionreport_background_draft2.png";
					}
					
					if(PersistDataListener.BASE_PACKAGE.equals(name))
						if(StringUtils.startsWith(instanceCode, "Iesa"))
							return (T) IesaBusinessLayer.class.getPackage();
				}
				
				if(SmtpProperties.class.equals(aClass)){
					if(SmtpProperties.FIELD_FROM.equals(name))
						return (T) "kycdev@gmail.com";
					if(SmtpProperties.FIELD_HOST.equals(name))
						return (T) "smtp@gmail.com";
					if(SmtpProperties.FIELD_PORT.equals(name))
						return (T) new Integer(465);
					if(commonUtils.attributePath(SmtpProperties.FIELD_CREDENTIALS, Credentials.FIELD_USERNAME).equals(name))
						return (T) "kycdev@gmail.com";
					if(commonUtils.attributePath(SmtpProperties.FIELD_CREDENTIALS, Credentials.FIELD_USERNAME).equals(name))
						return (T) "p@ssw0rd*";
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});
		
		CompanyBusinessLayer.Listener.COLLECTION.add(new CompanyBusinessLayer.Listener.Adapter() {
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
		PersonBusiness.FindNamesArguments.FIRST_NAME_IS_FIRST = Boolean.FALSE;
		StudentClassroomSessionDivisionBusiness.EVALUATION_TYPE_CODES
		.addAll(Arrays.asList(IesaConstant.EVALUATION_TYPE_TEST1,IesaConstant.EVALUATION_TYPE_TEST2,IesaConstant.EVALUATION_TYPE_EXAM));
		StudentClassroomSessionDivisionBusiness.SUM_MARKS[0] = Boolean.TRUE;
		
    	StudentBusinessImpl.Listener.COLLECTION.add(new StudentBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning(){
    		private static final long serialVersionUID = 1L;

			@Override
			public void beforeCreate(Student student) {
				super.beforeCreate(student);
				if(StringUtils.isBlank(student.getCode())){
					NumberBusiness.FormatArguments orderNumberFormatArguments = new FormatArguments();
					orderNumberFormatArguments.setWidth(4);
					student.setCode(IesaConstant.IESA+Constant.CHARACTER_SLASH+inject(TimeBusiness.class).findYear(inject(AcademicSessionBusiness.class).findCurrent(null).getBirthDate())
							+inject(PersonBusiness.class).findInitials(student.getPerson())+inject(NumberBusiness.class).format(inject(StudentDao.class).countAll()+1,orderNumberFormatArguments)
							+Constant.CHARACTER_HYPHEN+student.getAdmissionLevelTimeDivision().getLevel().getGroup().getCode()
							);
				}
			}
    	});
    	
    	TeacherBusinessImpl.Listener.COLLECTION.add(new TeacherBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning());
    	EmployeeBusinessImpl.Listener.COLLECTION.add(new EmployeeBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning()); 
    	
    	ClassroomSessionDivisionSubjectBusinessImpl.Listener.COLLECTION.add(new ClassroomSessionDivisionSubjectBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning(){
			private static final long serialVersionUID = 1L;
    		@Override
    		public void afterCreate(ClassroomSessionDivisionSubject classroomSessionDivisionSubject) {
    			super.afterCreate(classroomSessionDivisionSubject);
    			Collection<ClassroomSessionDivisionSubjectEvaluationType> classroomSessionDivisionSubjectEvaluationTypes = new ArrayList<>();
    			for(EvaluationType evaluationType : inject(EvaluationTypeBusiness.class).findAll()){
    				ClassroomSessionDivisionSubjectEvaluationType classroomSessionDivisionSubjectEvaluationType = new ClassroomSessionDivisionSubjectEvaluationType();
    				classroomSessionDivisionSubjectEvaluationType.setClassroomSessionDivisionSubject(classroomSessionDivisionSubject);
    				classroomSessionDivisionSubjectEvaluationType.setEvaluationType(evaluationType);
    				classroomSessionDivisionSubjectEvaluationType.setMaximumValue(new BigDecimal("100"));
    				classroomSessionDivisionSubjectEvaluationType.setCountInterval(inject(IntervalBusiness.class).instanciateOne(null, null, "1", "1"));
    				classroomSessionDivisionSubjectEvaluationTypes.add(classroomSessionDivisionSubjectEvaluationType);
    			}
    			inject(ClassroomSessionDivisionSubjectEvaluationTypeBusiness.class).create(classroomSessionDivisionSubjectEvaluationTypes);
    		}
    	});
    	SchoolConstant.Code.LevelGroup.KINDERGARTEN = "KS";
		SchoolConstant.Code.LevelGroup.PRIMARY = "PS";
		SchoolConstant.Code.LevelGroup.SECONDARY = "HS";
		
		AbstractIdentifiableBusinessServiceImpl.addAutoSetPropertyValueClass(new String[]{GlobalIdentifier.FIELD_CODE,GlobalIdentifier.FIELD_NAME}
			,AcademicSession.class,Level.class,LevelTimeDivision.class,ClassroomSession.class,ClassroomSessionDivision.class,ClassroomSessionDivisionSubject.class
			,ClassroomSessionDivisionSubjectEvaluationType.class,StudentClassroomSession.class,StudentClassroomSessionDivision.class
			,StudentClassroomSessionDivisionSubject.class);
		
		FileIdentifiableGlobalIdentifier.define(Student.class);
		FileIdentifiableGlobalIdentifier.define(Employee.class);
		FileIdentifiableGlobalIdentifier.define(Teacher.class);
		FileIdentifiableGlobalIdentifier.define(StudentClassroomSessionDivision.class);
	}
	
	@Override @SuppressWarnings("unchecked")
	protected void persistStructureData(){
		updateEnumeration(PersonTitle.class, PersonTitle.MISTER, "Mr.");
		updateEnumeration(PersonTitle.class, PersonTitle.MISS, "Ms.");
		updateEnumeration(PersonTitle.class, PersonTitle.MADAM, "Mrs.");
		
		updateEnumeration(Sex.class, Sex.MALE, "Male");
		updateEnumeration(Sex.class, Sex.FEMALE, "Female");
		
		LevelGroupType levelGroupType = create(inject(LevelGroupTypeBusiness.class).instanciateOne("LevelGroupTypeDummy"));
		LevelGroup levelGroupKindergarten = (LevelGroup) create(inject(LevelGroupBusiness.class).instanciateOne(SchoolConstant.Code.LevelGroup.KINDERGARTEN)
				.setType(levelGroupType));
		LevelGroup levelGroupPrimary = (LevelGroup) create(inject(LevelGroupBusiness.class).instanciateOne(SchoolConstant.Code.LevelGroup.PRIMARY)
				.setType(levelGroupType));
		LevelGroup levelGroupSecondary = (LevelGroup) create(inject(LevelGroupBusiness.class).instanciateOne(SchoolConstant.Code.LevelGroup.SECONDARY)
				.setType(levelGroupType));
		
    	evaluationTypeTest1 = create(inject(EvaluationTypeBusiness.class).instanciateOne(IesaConstant.EVALUATION_TYPE_TEST1,"Test 1"));
    	evaluationTypeTest2 = create(inject(EvaluationTypeBusiness.class).instanciateOne(IesaConstant.EVALUATION_TYPE_TEST2,"Test 2"));
    	evaluationTypeExam = create(inject(EvaluationTypeBusiness.class).instanciateOne(IesaConstant.EVALUATION_TYPE_EXAM,"Exam"));
    	
    	File documentHeaderFile = inject(FileDao.class).read(CompanyConstant.FILE_DOCUMENT_HEADER); 
    	File documentBackgroundImageFile = inject(FileDao.class).read(CompanyConstant.FILE_DOCUMENT_BACKGROUND); 
    	File documentBackgroundImageDraftFile = inject(FileDao.class).read(CompanyConstant.FILE_DOCUMENT_BACKGROUND_DRAFT);
    	
    	ReportTemplate reportTemplatePk = rootDataProducerHelper.createReportTemplate("IesaReportTemplatePK","Report Sheet",Boolean.TRUE,"report/studentclassroomsessiondivision/pkg.jrxml",documentHeaderFile
				, documentBackgroundImageFile, documentBackgroundImageDraftFile);
    	
    	ReportTemplate reportTemplateKg1 = rootDataProducerHelper.createReportTemplate("IesaReportTemplateKG1","Report Sheet",Boolean.TRUE,"report/studentclassroomsessiondivision/kg1.jrxml",documentHeaderFile
				, documentBackgroundImageFile, documentBackgroundImageDraftFile);
    	
    	ReportTemplate reportTemplateKg2Kg3 = rootDataProducerHelper.createReportTemplate("IesaReportTemplateKG2KG3","Report Sheet",Boolean.TRUE,"report/studentclassroomsessiondivision/kg2kg3.jrxml",documentHeaderFile
				, documentBackgroundImageFile, documentBackgroundImageDraftFile);
    	
		ReportTemplate reportTemplateG1G12 = rootDataProducerHelper.createReportTemplate("IesaReportTemplateG1G12", "Report Sheet"
				, Boolean.TRUE, "report/studentclassroomsessiondivision/g1g12.jrxml", documentHeaderFile, documentBackgroundImageFile, documentBackgroundImageDraftFile); 
    	
    	Interval interval = inject(IntervalBusiness.class).instanciateOne(null, "A", "1", "2");
		create(interval);
    	
		String classroomSessionDivisionIndex = "1";
		
    	CommonNodeInformations commonNodeInformationsPkg = instanciateCommonNodeInformations(null,null, reportTemplatePk,classroomSessionDivisionIndex,interval);
    	CommonNodeInformations commonNodeInformationsKg1 = instanciateCommonNodeInformations(null,null, reportTemplateKg1,classroomSessionDivisionIndex,interval);
    	CommonNodeInformations commonNodeInformationsKg2Kg3 = instanciateCommonNodeInformations(null,null, reportTemplateKg2Kg3,classroomSessionDivisionIndex,interval);
    	
		CommonNodeInformations commonNodeInformationsG1G3 = instanciateCommonNodeInformations(create(inject(IntervalCollectionBusiness.class)
				.instanciateOne("ICEV1", "Grading Scale", new String[][]{
						{"A+", "Excellent", "90", "100"},{"A", "Very good", "80", "89.99"},{"B+", "Good", "70", "79.99"},{"B", "Fair", "60", "69.99"}
						,{"C+", "Satisfactory", "55", "59.99"},{"C", "Barely satisfactory", "50", "54.99"},{"E", "Fail", "0", "49.99"}})),
				create(inject(IntervalCollectionBusiness.class)
						.instanciateOne("ICP1", "Promotion Scale", new String[][]{
								{"P", "Promoted", "50", "100"},{"PT", "Promoted on trial", "45", "49.99"},{"NP", "Not promoted", "0", "44.99"}})),reportTemplateG1G12, classroomSessionDivisionIndex,interval);
		CommonNodeInformations commonNodeInformationsG4G6 = commonNodeInformationsG1G3;
		
		CommonNodeInformations commonNodeInformationsG7G9 = instanciateCommonNodeInformations(create(inject(IntervalCollectionBusiness.class)
				.instanciateOne("ICEV2", "Grading Scale", new String[][]{
						{"A*", "Outstanding", "90", "100"},{"A", "Excellent", "80", "89.99"},{"B", "Very Good", "70", "79.99"},{"C", "Good", "60", "69.99"}
						,{"D", "Satisfactory", "50", "59.99"},{"E", "Fail", "0", "49.99"}})),
				create(inject(IntervalCollectionBusiness.class)
						.instanciateOne("ICP2", "Promotion Scale", new String[][]{
								{"P", "Promoted", "50", "100"},{"PT", "Promoted on trial", "45", "49.99"},{"NP", "Not promoted", "0", "44.99"}})),reportTemplateG1G12, classroomSessionDivisionIndex,interval);	
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
    	academicSession.setBirthDate(new DateTime(2016, 10, 1, 0, 0).toDate());
    	academicSession.setDeathDate(new DateTime(2017, 6, 1, 0, 0).toDate());
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
    	Collection<MetricCollectionIdentifiableGlobalIdentifier> metricCollectionIdentifiableGlobalIdentifiers = new ArrayList<>(); 
    	
    	Long gradeIndex = 0l;
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.PK,"Pre-Kindergarten",levelGroupKindergarten,commonNodeInformationsPkg,gradeIndex++) 
    			,null, null,null,null,new String[]{SchoolConstant.Code.MetricCollection.ATTENDANCE_KINDERGARTEN_STUDENT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_EXPRESSIVE_LANGUAGE
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_RECEPTIVE_LANGUAGE
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_READING_READNESS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_NUMERACY_DEVELOPMENT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_ARTS_MUSIC
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_SOCIAL_EMOTIONAL_DEVELOPMENT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_GROSS_MOTOR_SKILLS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_PK_STUDENT_FINE_MOTOR_SKILLS
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_KINDERGARTEN_STUDENT},Boolean.FALSE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.K1,"Kindergarten 1",levelGroupKindergarten,commonNodeInformationsKg1,gradeIndex++) 
    			, null,null,null,null,new String[]{SchoolConstant.Code.MetricCollection.ATTENDANCE_KINDERGARTEN_STUDENT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_ENGLISH_LANGUAGE_ARTS_READING
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_COMMUNICATION_SKILLS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_SCIENCE
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_SOCIAL_STUDIES
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_MATHEMATICS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_WORK_HABITS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K1_STUDENT_SOCIAL_SKILLS
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_KINDERGARTEN_STUDENT},Boolean.FALSE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.K2,"Kindergarten 2",levelGroupKindergarten,commonNodeInformationsKg2Kg3,gradeIndex++) 
    			, null,null,null,null,new String[]{SchoolConstant.Code.MetricCollection.ATTENDANCE_KINDERGARTEN_STUDENT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_READING_READINESS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_READING
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_WRITING
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_LISTENING_SPEAKING_VIEWING
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_ALPHABET_IDENTIFICATION
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_MATHEMATICS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_SCIENCE_SOCIAL_STUDIES_MORAL_EDUCATION
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_ART_CRAFT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_MUSIC
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_PHYSICAL_EDUCATION
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_WORK_BEHAVIOUR_HABITS
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_KINDERGARTEN_STUDENT},Boolean.FALSE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.K3,"Kindergarten 3",levelGroupKindergarten,commonNodeInformationsKg2Kg3,gradeIndex++) 
    			, null,null,null,null,new String[]{SchoolConstant.Code.MetricCollection.ATTENDANCE_KINDERGARTEN_STUDENT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_READING_READINESS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_READING
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_WRITING
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_LISTENING_SPEAKING_VIEWING
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_ALPHABET_IDENTIFICATION
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_MATHEMATICS
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_SCIENCE_SOCIAL_STUDIES_MORAL_EDUCATION
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_ART_CRAFT
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_MUSIC
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_PHYSICAL_EDUCATION
    					,SchoolConstant.Code.MetricCollection.BEHAVIOUR_KINDERGARTEN_K2_STUDENT_WORK_BEHAVIOUR_HABITS
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_KINDERGARTEN_STUDENT},Boolean.FALSE,Boolean.FALSE);
    	
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G1,"Grade 1",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++),null 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}}, subjectsG1G3
    			,new String[]{"A","B"},new String[]{SchoolConstant.Code.MetricCollection.BEHAVIOUR_PRIMARY_STUDENT
    					,SchoolConstant.Code.MetricCollection.ATTENDANCE_STUDENT
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_STUDENT},Boolean.TRUE,Boolean.TRUE);    	
    	/*
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G2,"Grade 2",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}}, subjectsG1G3
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G3,"Grade 3",levelGroupPrimary,commonNodeInformationsG1G3,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG1G3
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	*/
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G4,"Grade 4",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++),null 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6
    			,new String[]{"A","B"},new String[]{SchoolConstant.Code.MetricCollection.BEHAVIOUR_PRIMARY_STUDENT
    					,SchoolConstant.Code.MetricCollection.ATTENDANCE_STUDENT
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_STUDENT},Boolean.TRUE,Boolean.TRUE);
    	
    	/*schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G5,"Grade 5",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,new String[]{"A","B"},Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G6,"Grade 6",levelGroupPrimary,commonNodeInformationsG4G6,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG4G6
    			,classroomSessionDivisionStudentsMetricCollections,g1g6MetricCollections,null,Boolean.TRUE,Boolean.TRUE);
    	*/
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G7,"Grade 7",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++),null 
    			, new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG7G8
    			,null,new String[]{SchoolConstant.Code.MetricCollection.BEHAVIOUR_PRIMARY_STUDENT
    					,SchoolConstant.Code.MetricCollection.ATTENDANCE_STUDENT
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_STUDENT},Boolean.TRUE,Boolean.TRUE);
    	/*schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G8,"Grade 8",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG7G8
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.TRUE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G9,"Grade 9",levelGroupSecondary,commonNodeInformationsG7G9,gradeIndex++) 
    			,new Object[][]{{evaluationTypeTest1,"0.15","100"},{evaluationTypeTest2,"0.15","100"},{evaluationTypeExam,"0.7","100"}},subjectsG9G12
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.FALSE);
    	*/
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes
    			,metricCollectionIdentifiableGlobalIdentifiers,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G10,"Grade 10",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++),null 
    			, new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG9G12
    			,null,new String[]{SchoolConstant.Code.MetricCollection.BEHAVIOUR_PRIMARY_STUDENT
    					,SchoolConstant.Code.MetricCollection.ATTENDANCE_STUDENT
    					,SchoolConstant.Code.MetricCollection.COMMUNICATION_STUDENT},Boolean.TRUE,Boolean.FALSE);
    	/*schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G11,"Grade 11",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG9G12
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.FALSE);
    	schoolDataProducerHelper.instanciateOneClassroomSession(classroomSessions,classroomSessionDivisions,classroomSessionDivisionSubjects,subjectEvaluationTypes,academicSession
    			, schoolDataProducerHelper.createLevelTimeDivision(SchoolConstant.Code.LevelName.G12,"Grade 12",levelGroupSecondary,commonNodeInformationsG10G12,gradeIndex++) 
    			, new Object[][]{{evaluationTypeTest1,"0.2","100"},{evaluationTypeTest2,"0.2","100"},{evaluationTypeExam,"0.6","100"}},subjectsG9G12
    			,classroomSessionDivisionStudentsMetricCollections,g7g12MetricCollections,null,Boolean.TRUE,Boolean.FALSE);
    	*/
    	inject(ClassroomSessionBusiness.class).create(classroomSessions);
    	inject(ClassroomSessionDivisionBusiness.class).create(classroomSessionDivisions);
    	inject(ClassroomSessionDivisionSubjectBusiness.class).create(classroomSessionDivisionSubjects);
    	inject(ClassroomSessionDivisionSubjectEvaluationTypeBusiness.class).create(subjectEvaluationTypes);
    	inject(MetricCollectionIdentifiableGlobalIdentifierBusiness.class).create(metricCollectionIdentifiableGlobalIdentifiers);
    	
    	for(AbstractIdentifiable identifiable :  genericDao.use(EvaluationType.class).select().all())
    		StudentClassroomSessionDivisionBusiness.EVALUATION_TYPE_CODES.add(((EvaluationType)identifiable).getCode());
    	StudentClassroomSessionDivisionBusiness.SUM_MARKS[0] = Boolean.TRUE;
    	
    	/* reading data from excel files */
	}
	
	/**/
	
	@Override
	protected void setConstants(){}
	
	public static IesaBusinessLayer getInstance() {
		return INSTANCE;
	}

	/**/
	
	protected void fakeTransactions(){}
	
	/**/
	
	private CommonNodeInformations instanciateCommonNodeInformations(IntervalCollection intervalCollection,IntervalCollection promotionIntervalCollection
			,ReportTemplate reportTemplate,String classroomsessionDivisionIndex,Interval interval){
		CommonNodeInformations commonNodeInformations = schoolDataProducerHelper.instanciateOneCommonNodeInformations(intervalCollection,promotionIntervalCollection, reportTemplate
				, TimeDivisionType.DAY, TimeDivisionType.TRIMESTER,"50", classroomsessionDivisionIndex,interval);
		return commonNodeInformations;
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
