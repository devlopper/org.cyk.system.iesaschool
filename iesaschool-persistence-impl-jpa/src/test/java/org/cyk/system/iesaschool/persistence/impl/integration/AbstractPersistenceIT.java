package org.cyk.system.iesaschool.persistence.impl.integration;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import lombok.Getter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.persistence.api.GenericDao;
import org.cyk.system.root.persistence.impl.AbstractPersistenceService;
import org.cyk.utility.test.integration.AbstractIntegrationTestJpaBased;
import org.cyk.utility.test.TestMethod;
import org.jboss.shrinkwrap.api.Archive;

/**
 * Persistence integration test (IT)
 * @author Komenan Y .Christian
 *
 */ 
public abstract class AbstractPersistenceIT extends AbstractIntegrationTestJpaBased {
	 
	private static final long serialVersionUID = -3977685343817022628L;
	
	@Inject @Getter private GenericDao genericDao;
		
	@Override
    protected void _execute_() {
        super._execute_();
        transaction(new TestMethod() {private static final long serialVersionUID = 1L; @Override protected void test() { create(); } });    
        read(); 
        transaction(new TestMethod() {private static final long serialVersionUID = 1L; @Override protected void test() { update(); } });    
        transaction(new TestMethod() {private static final long serialVersionUID = 1L; @Override protected void test() { delete(); } });    
        queries();
    }
	
	/**/
	
	@Override
	public EntityManager getEntityManager() {
	    return ((AbstractPersistenceService<?>)genericDao).getEntityManager();
	}
	
	protected abstract void queries();
	
	/* Shortcut */
	
	protected AbstractIdentifiable create(AbstractIdentifiable object){
		return genericDao.create(object);
	}
	
	protected AbstractIdentifiable update(AbstractIdentifiable object){
		return genericDao.update(object);
	}
	
	public static Archive<?> createRootDeployment() {
        return _deploymentOfPackages("org.cyk.system.iesaschool").getArchive();
    } 
	
}
