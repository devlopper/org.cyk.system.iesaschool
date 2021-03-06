package org.cyk.system.iesaschool.business.impl.integration;

import java.math.BigDecimal;
import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.iesaschool.business.impl.IesaBusinessTestHelper;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.root.business.impl.BusinessIntegrationTestHelper;
import org.cyk.system.root.business.impl.PersistDataListener;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.business.impl.validation.DefaultValidator;
import org.cyk.system.root.business.impl.validation.ExceptionUtils;
import org.cyk.system.root.business.impl.validation.ValidatorMap;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.persistence.impl.GenericDaoImpl;
import org.cyk.system.root.persistence.impl.PersistenceIntegrationTestHelper;
import org.cyk.system.school.business.impl.SchoolBusinessLayer;
import org.cyk.system.school.business.impl._dataproducer.IesaFakedDataProducer;
import org.cyk.system.school.business.impl._test.SchoolBusinessTestHelper;
import org.cyk.system.school.business.impl.session.LevelBusinessImpl;
import org.cyk.system.school.model.SchoolConstant;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.LevelTimeDivision;
import org.cyk.system.school.persistence.api.session.ClassroomSessionDao;
import org.cyk.system.school.persistence.api.session.LevelTimeDivisionDao;
import org.cyk.system.school.persistence.api.subject.ClassroomSessionDivisionSubjectDao;
import org.cyk.utility.common.test.TestEnvironmentListener;
import org.cyk.utility.test.ArchiveBuilder;
import org.cyk.utility.test.Transaction;
import org.cyk.utility.test.integration.AbstractIntegrationTestJpaBased;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

public abstract class AbstractBusinessIT extends AbstractIntegrationTestJpaBased {

	private static final long serialVersionUID = -5752455124275831171L;
	
	static {
		TestEnvironmentListener.COLLECTION.add(new TestEnvironmentListener.Adapter.Default(){
			private static final long serialVersionUID = 1L;
			@Override
    		public void assertEquals(String message, Object expected, Object actual) {
    			Assert.assertEquals(message, expected, actual);
    		}
    		@Override
    		public String formatBigDecimal(BigDecimal value) {
    			return inject(NumberBusiness.class).format(value);
    		}
    	});
	}
	
	@Inject protected ExceptionUtils exceptionUtils; 
	@Inject protected DefaultValidator defaultValidator;
	@Inject protected GenericDaoImpl g;
	@Inject protected GenericBusiness genericBusiness;
	@Inject protected ApplicationBusiness applicationBusiness;
	
	@Inject protected ValidatorMap validatorMap;// = ValidatorMap.getInstance();
	@Inject protected RootBusinessLayer rootBusinessLayer;
	@Inject protected RootBusinessTestHelper rootTestHelper;
	@Inject protected RootDataProducerHelper rootDataProducerHelper;
	
	@Inject protected SchoolBusinessTestHelper schoolBusinessTestHelper;
	@Inject protected SchoolBusinessLayer schoolBusinessLayer;
	@Inject protected IesaBusinessLayer iesaBusinessLayer;
	@Inject protected IesaBusinessTestHelper iesaBusinessTestHelper;
	
	@Inject protected ClassroomSessionDao classroomSessionDao;
	@Inject protected ClassroomSessionDivisionSubjectDao classroomSessionDivisionSubjectDao;
	@Inject protected LevelTimeDivisionDao levelTimeDivisionDao;
	
	@Inject protected UserTransaction userTransaction;
    
	@Deployment
    public static Archive<?> createDeployment() {
    	Archive<?> archive = createRootDeployment();
    	return archive;
    }
	
	protected void installApplication(Boolean fake){
		iesaBusinessLayer.installApplication(fake);
    }
    
    protected void installApplication(){
    	PersistDataListener.COLLECTION.add(new PersistDataListener.Adapter.Default(){
			private static final long serialVersionUID = -950053441831528010L;
			@SuppressWarnings("unchecked")
			@Override
			public <T> T processPropertyValue(Class<?> aClass,String instanceCode, String name, T value) {
				/*if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_HEADER}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "/report/iesa/salecashregistermovementlogo.png";
				}*/
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_HEADER}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name)){
						return (T) "/report/iesa/document_header.png";
					}else if(PersistDataListener.BASE_PACKAGE.equals(name))
						return (T) SchoolBusinessLayer.class.getPackage();
				}
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_BACKGROUND}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "/report/iesa/studentclassroomsessiondivisionreport_background.jpg";
					else if(PersistDataListener.BASE_PACKAGE.equals(name))
						return (T) SchoolBusinessLayer.class.getPackage();
				}
				if(ArrayUtils.contains(new String[]{CompanyConstant.Code.File.DOCUMENT_BACKGROUND_DRAFT}, instanceCode)){
					if(PersistDataListener.RELATIVE_PATH.equals(name))
						return (T) "/report/iesa/studentclassroomsessiondivisionreport_background.jpg";
					else if(PersistDataListener.BASE_PACKAGE.equals(name))
						return (T) SchoolBusinessLayer.class.getPackage();
				}
				return super.processPropertyValue(aClass, instanceCode, name, value);
			}
		});
    	/*
    	SchoolConstant.Code.LevelGroup.KINDERGARTEN = "KS";
		SchoolConstant.Code.LevelGroup.PRIMARY = "PS";
		SchoolConstant.Code.LevelGroup.SECONDARY = "HS";
		*/
		
		/*SchoolDataProducerHelper.Listener.COLLECTION.add(new SchoolDataProducerHelper.Listener.Adapter(){
			private static final long serialVersionUID = -5322009577688489872L;
			@Override
			public void classroomSessionDivisionCreated(ClassroomSessionDivision classroomSessionDivision) {
				if(classroomSessionDivision.getOrderNumber()==1){
					classroomSessionDivision.getExistencePeriod().getNumberOfMillisecond().set(63l * DateTimeConstants.MILLIS_PER_DAY);
				}else if(classroomSessionDivision.getOrderNumber()==2){
					classroomSessionDivision.getExistencePeriod().setFromDate(new DateTime(2017, 1, 9, 0, 0).toDate());
	    			classroomSessionDivision.getExistencePeriod().setToDate(new DateTime(2017, 3, 27, 0, 0).toDate());
				}
				classroomSessionDivision.setStudentSubjectAttendanceAggregated(Boolean.FALSE);
			}
			
			@Override
			public void classroomSessionDivisionSubjectEvaluationTypeCreated(ClassroomSessionDivisionSubjectEvaluationType classroomSessionDivisionSubjectEvaluationType) {
				super.classroomSessionDivisionSubjectEvaluationTypeCreated(classroomSessionDivisionSubjectEvaluationType);
				classroomSessionDivisionSubjectEvaluationType.setMaximumValue(new BigDecimal("100"));
				classroomSessionDivisionSubjectEvaluationType.setCountInterval(inject(IntervalDao.class).read(SchoolConstant.Code.Interval.EVALUATION_COUNT_BY_TYPE));
			}
		});
    	ClassroomSessionDivisionSubjectBusinessImpl.Listener.COLLECTION.clear();
    	*/
    	long t = System.currentTimeMillis();
    	SchoolConstant.Configuration.Evaluation.COEFFICIENT_APPLIED = Boolean.FALSE;
    	installApplication(Boolean.TRUE);
    	produce(getFakedDataProducer());
    	System.out.println( ((System.currentTimeMillis()-t)/1000)+" s" );
    }
	
    protected AbstractFakedDataProducer getFakedDataProducer(){
    	return inject(IesaFakedDataProducer.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return g.getEntityManager();
    }
	
    @Override
    protected void _execute_() {
        super._execute_();
        create();    
        read(); 
        update();    
        delete();    
        finds();
        businesses();
    }
    
	protected void finds(){}
	
	protected void businesses(){}
	
	/* Shortcut */
    
    protected AbstractIdentifiable create(AbstractIdentifiable object){
        return genericBusiness.create(object);
    }
    
    protected AbstractIdentifiable update(AbstractIdentifiable object){
        return genericBusiness.update(object);
    }
        
    public static Archive<?> createRootDeployment() {
        return  
                new ArchiveBuilder().create().getArchive().
                addClasses(BusinessIntegrationTestHelper.classes()).
                addClasses(PersistenceIntegrationTestHelper.classes()).
                addClasses(RootBusinessLayer.class,RootBusinessTestHelper.class,CompanyBusinessLayer.class).
                addPackages(Boolean.FALSE, BusinessIntegrationTestHelper.packages()).
                    addPackages(Boolean.TRUE,"org.cyk.system.company").
                    addPackages(Boolean.TRUE,"org.cyk.system.school"). 
                    addPackages(Boolean.TRUE,"org.cyk.system.iesaschool") 
                ;
    } 
    
    @Override 
    protected void populate() {
    	LevelBusinessImpl.PROPERTY_VALUE_TOKENS_CONCATENATE_WITH_GROUP_LEVELNAME_SPECIALITY = Boolean.FALSE;
    	//LevelTimeDivisionBusinessImpl.PROPERTY_VALUE_TOKENS_CONCATENATE_WITH_TIMEDIVISIONTYPE = Boolean.FALSE;
    	
    	installApplication();
    }
    
    @Override
    protected Boolean populateInTransaction() {
    	return Boolean.FALSE;
    }
    
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}

    protected AbstractFakedDataProducer.Listener fakedDataProducerAdapter(){
    	return new AbstractFakedDataProducer.Listener.Adapter.Default(){
			private static final long serialVersionUID = 1L;
			@Override
    		public void flush() {
    			super.flush();
    			getEntityManager().flush();
    		}
    	};
    }
    
    protected void produce(final AbstractFakedDataProducer fakedDataProducer){
    	if(fakedDataProducer==null)
    		return ;
    	new Transaction(this,userTransaction,null){
			@Override
			public void _execute_() {
				fakedDataProducer.produce(fakedDataProducerAdapter());
				//getEntityManager().flush();
			}
    	}.run();
    }

    protected Collection<ClassroomSession> getClassroomSessions(Long index){
    	return classroomSessionDao.readByLevelTimeDivision(levelTimeDivisionDao.readByGlobalIdentifierOrderNumber(index).iterator().next());
    }
    
    protected ClassroomSession getClassroomSessionOne(Long index){
    	Collection<LevelTimeDivision> levelTimeDivisions = levelTimeDivisionDao.readByGlobalIdentifierOrderNumber(index);
    	if(levelTimeDivisions.isEmpty())
    		return null;
    	Collection<ClassroomSession> classroomSessions = classroomSessionDao.readByLevelTimeDivision(levelTimeDivisions.iterator().next());
    	return classroomSessions.isEmpty() ? null : classroomSessions.iterator().next();
    }
}