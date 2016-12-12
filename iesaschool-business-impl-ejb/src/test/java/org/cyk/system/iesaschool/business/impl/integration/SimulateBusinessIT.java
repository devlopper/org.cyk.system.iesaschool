package org.cyk.system.iesaschool.business.impl.integration;

public class SimulateBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Override
    protected void businesses() {
    	installApplication();
    	/*
    	SchoolBusinessSimulationParameters parameters = new SchoolBusinessSimulationParameters();

    	//AbstractBean.SYSTEM_OUT_LOG_TRACE = Boolean.TRUE;
    	parameters.setGeneratedClassroomSessionCountByLevel(1);
    	parameters.getClassroomSessionDivisionIndexes().add(0);
    	parameters.setCreateStudentClassroomSessionDivisionReport(Boolean.TRUE);
    	schoolBusinessTestHelper.setCustomClassroomSessionDivisionSubjectEvaluationTypeInfos(new Object[][]{
    		{IesaBusinessLayer.getInstance().getEvaluationTypeTest1(),"0.15","100"},{IesaBusinessLayer.getInstance().getEvaluationTypeTest2(),"0.15","100"}
    		,{IesaBusinessLayer.getInstance().getEvaluationTypeExam(),"0.7","100"}
    	});
    	schoolBusinessTestHelper.simulate(parameters);
    	*/
    }
    
}
