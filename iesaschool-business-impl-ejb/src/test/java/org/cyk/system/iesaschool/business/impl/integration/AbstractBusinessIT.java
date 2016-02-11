package org.cyk.system.iesaschool.business.impl.integration;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.iesaschool.business.impl.IesaBusinessLayer;
import org.cyk.system.iesaschool.business.impl.IesaBusinessTestHelper;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer.FakedDataProducerAdapter;
import org.cyk.system.root.business.impl.BusinessIntegrationTestHelper;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.business.impl.validation.DefaultValidator;
import org.cyk.system.root.business.impl.validation.ExceptionUtils;
import org.cyk.system.root.business.impl.validation.ValidatorMap;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.persistence.impl.GenericDaoImpl;
import org.cyk.system.root.persistence.impl.PersistenceIntegrationTestHelper;
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
    			return RootBusinessLayer.getInstance().getNumberBusiness().format(value);
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
	
	@Inject protected IesaBusinessLayer iesaschoolBusinessLayer;
	@Inject protected IesaBusinessTestHelper iesaschoolBusinessTestHelper;
	
	@Inject protected UserTransaction userTransaction;
    
	@Deployment
    public static Archive<?> createDeployment() {
    	Archive<?> archive = createRootDeployment();
    	return archive;
    }
	
	protected void installApplication(Boolean fake){
    	iesaschoolBusinessLayer.installApplication(fake);
    }
    
    protected void installApplication(){
    	long t = System.currentTimeMillis();
    	installApplication(Boolean.TRUE);
    	produce(getFakedDataProducer());
    	System.out.println( ((System.currentTimeMillis()-t)/1000)+" s" );
    }
	
    protected AbstractFakedDataProducer getFakedDataProducer(){
    	return null;
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
	
	protected abstract void businesses();
	
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
    
    @Override protected void populate() {}
    
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}

    protected FakedDataProducerAdapter fakedDataProducerAdapter(){
    	return new FakedDataProducerAdapter(){
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
}