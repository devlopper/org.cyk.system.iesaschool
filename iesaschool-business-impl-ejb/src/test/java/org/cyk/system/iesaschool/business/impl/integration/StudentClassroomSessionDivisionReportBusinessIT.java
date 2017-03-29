package org.cyk.system.iesaschool.business.impl.integration;

import org.cyk.system.root.business.impl.AbstractFakedDataProducer;
import org.cyk.system.school.business.impl._dataproducer.IesaFakedDataProducer;
import org.cyk.system.school.model.SchoolConstant;

public class StudentClassroomSessionDivisionReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	Object[][] objects = new Object[][]{
    		/*{SchoolConstant.Code.LevelName.PK,null,1l}
    		,{SchoolConstant.Code.LevelName.K1,null,1l}
    		,{SchoolConstant.Code.LevelName.K2,null,1l}
    		,{SchoolConstant.Code.LevelName.K3,null,1l}*/
    		/*,*/{SchoolConstant.Code.LevelName.G1,SchoolConstant.Code.ClassroomSessionSuffix.A,1l}
    		,{SchoolConstant.Code.LevelName.G1,SchoolConstant.Code.ClassroomSessionSuffix.A,2l}
    		,{SchoolConstant.Code.LevelName.G1,SchoolConstant.Code.ClassroomSessionSuffix.A,3l}
    		//,{SchoolConstant.Code.LevelName.G8,SchoolConstant.Code.ClassroomSessionSuffix.A,1l}
    		//,{SchoolConstant.Code.LevelName.G9,null,1l}
    		//,{SchoolConstant.Code.LevelName.G9,null,2l}
    		//,{SchoolConstant.Code.LevelName.G9,null,3l}
    	};
    	
    	//schoolBusinessTestHelper.generateStudentClassroomSessionDivisionReport(objects, new Boolean[]{Boolean.FALSE},Boolean.TRUE, Boolean.FALSE);
    	
    }
    
    @Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	IesaFakedDataProducer dataProducer = (IesaFakedDataProducer) super.getFakedDataProducer().setDoBusiness(Boolean.TRUE);
    	dataProducer.getClassroomSessionLevelTimeDivisionCodes().add(SchoolConstant.Code.LevelTimeDivision.PK_YEAR_1);
    	dataProducer.getClassroomSessionLevelTimeDivisionCodes().add(SchoolConstant.Code.LevelTimeDivision.K1_YEAR_1);
    	dataProducer.getClassroomSessionLevelTimeDivisionCodes().add(SchoolConstant.Code.LevelTimeDivision.K2_YEAR_1);
    	dataProducer.getClassroomSessionLevelTimeDivisionCodes().add(SchoolConstant.Code.LevelTimeDivision.K3_YEAR_1);
    	dataProducer.getClassroomSessionLevelTimeDivisionCodes().add(SchoolConstant.Code.LevelTimeDivision.G1_YEAR_1);
    	dataProducer.getClassroomSessionLevelTimeDivisionCodes().add(SchoolConstant.Code.LevelTimeDivision.G8_YEAR_1);
    	dataProducer.getClassroomSessionLevelTimeDivisionCodes().add(SchoolConstant.Code.LevelTimeDivision.G9_YEAR_1);
    	return dataProducer;
    }
        
}
