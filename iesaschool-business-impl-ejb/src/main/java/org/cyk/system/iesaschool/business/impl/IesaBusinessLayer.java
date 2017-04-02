package org.cyk.system.iesaschool.business.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.structure.CompanyBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.structure.EmployeeBusinessImpl;
import org.cyk.system.iesaschool.model.IesaConstant;
import org.cyk.system.root.business.api.message.MailBusiness;
import org.cyk.system.root.business.api.party.person.PersonBusiness;
import org.cyk.system.root.business.api.time.TimeBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.persistence.api.GenericDao;
import org.cyk.system.school.business.api.session.AcademicSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionBusiness;
import org.cyk.system.school.business.api.session.ClassroomSessionDivisionBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectBusiness;
import org.cyk.system.school.business.api.subject.ClassroomSessionDivisionSubjectEvaluationTypeBusiness;
import org.cyk.system.school.business.impl.AbstractSchoolReportProducer;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl.actor.StudentBusinessImpl;
import org.cyk.system.school.business.impl.actor.TeacherBusinessImpl;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.actor.Student;
import org.cyk.system.school.model.session.StudentClassroomSession;
import org.cyk.system.school.model.subject.Subject;
import org.cyk.system.school.persistence.api.actor.StudentDao;
import org.cyk.system.school.persistence.api.actor.TeacherDao;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;
import org.cyk.utility.common.formatter.NumberFormatter;
import org.cyk.utility.common.generator.AbstractGeneratable;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=IesaBusinessLayer.DEPLOYMENT_ORDER) @Getter
public class IesaBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = SchoolBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static IesaBusinessLayer INSTANCE;
	
	@Inject private RootBusinessLayer rootBusinessLayer;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private SchoolBusinessLayer schoolBusinessLayer;
	
	@Inject private OwnedCompanyBusiness ownedCompanyBusiness;
	@Inject private CompanyBusiness companyBusiness;
	@Inject private ClassroomSessionDivisionSubjectEvaluationTypeBusiness subjectEvaluationTypeBusiness;
	@Inject private ClassroomSessionBusiness classroomSessionBusiness;
	@Inject private ClassroomSessionDivisionBusiness classroomSessionDivisionBusiness;
	@Inject private ClassroomSessionDivisionSubjectBusiness classroomSessionDivisionSubjectBusiness;
	
	@Inject private GenericDao genericDao;
	@Inject private TeacherDao teacherDao;
	
	private ArrayList<Subject> subjectsG1G3 = new ArrayList<>(),subjectsG4G5 = new ArrayList<>(),subjectsG6 = new ArrayList<>()
			,subjectsG7G8 = new ArrayList<>(); 
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		/*PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
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
							return (T) "image/studentclassroomsessiondivisionreport_background_draft3.png";
					}
					
					if(PersistDataListener.BASE_PACKAGE.equals(name))
						if(StringUtils.startsWith(instanceCode, "Iesa"))
							return (T) IesaBusinessLayer.class.getPackage();
				}
				
				if(SmtpProperties.class.equals(aClass)){
					if(SmtpProperties.FIELD_FROM.equals(name))
						return (T) "results@iesaci.com";
					if(SmtpProperties.FIELD_HOST.equals(name))
						return (T) "smtp.iesaci.com";
					if(SmtpProperties.FIELD_PORT.equals(name))
						return (T) new Integer(25);
					if(commonUtils.attributePath(SmtpProperties.FIELD_CREDENTIALS, Credentials.FIELD_USERNAME).equals(name))
						return (T) "results@iesaci.com";
					if(commonUtils.attributePath(SmtpProperties.FIELD_CREDENTIALS, Credentials.FIELD_PASSWORD).equals(name))
						return (T) "school2009";
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});*/
		
		//inject(MailBusiness.class).setProperties("smtp.iesaci.com", 25, "results@iesaci.com", "school2009");//TODO should not be declare here but from database
		/*
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
		*/
		AbstractSchoolReportProducer.DEFAULT = new ReportProducer();
		PersonBusiness.FindNamesArguments.FIRST_NAME_IS_FIRST = Boolean.FALSE;
		RootConstant.Configuration.ReportTemplate.LOCALE = Locale.ENGLISH;
		AbstractGeneratable.Listener.Adapter.Default.LOCALE = Locale.ENGLISH;
		
		/*SchoolConstant.Code.EvaluationType.COLLECTION.addAll(Arrays.asList(SchoolConstant.Code.EvaluationType.TEST1,SchoolConstant.Code.EvaluationType.TEST2
				,SchoolConstant.Code.EvaluationType.EXAM));
		*/
		SchoolConstant.Configuration.Evaluation.SUM_ON_STUDENT_CLASSROOM_SESSION_DIVISION_REPORT = Boolean.TRUE;
		
    	StudentBusinessImpl.Listener.COLLECTION.add(new StudentBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning(){
    		private static final long serialVersionUID = 1L;

			@Override
			public void beforeCreate(Student student) {
				super.beforeCreate(student);
				if(StringUtils.isBlank(student.getCode()) && student.getAdmissionLevelTimeDivision()!=null){
					NumberFormatter.String.Adapter.Default orderNumberFormatter = new NumberFormatter.String.Adapter.Default(inject(StudentDao.class).countAll()+1,null);
					orderNumberFormatter.setWidth(4);
					student.setCode(IesaConstant.IESA+Constant.CHARACTER_SLASH+inject(TimeBusiness.class).findYear(inject(AcademicSessionBusiness.class).findCurrent(null).getBirthDate())
							+inject(PersonBusiness.class).findInitials(student.getPerson())+orderNumberFormatter.execute()
							+Constant.CHARACTER_HYPHEN+student.getAdmissionLevelTimeDivision().getLevel().getGroup().getCode()
							);
				}
			}
    	});
    	
    	TeacherBusinessImpl.Listener.COLLECTION.add(new TeacherBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning());
    	EmployeeBusinessImpl.Listener.COLLECTION.add(new EmployeeBusinessImpl.Listener.Adapter.Default.EnterpriseResourcePlanning()); 

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
