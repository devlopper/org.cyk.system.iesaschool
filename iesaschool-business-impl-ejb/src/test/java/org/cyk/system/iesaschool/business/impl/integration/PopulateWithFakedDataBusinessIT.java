package org.cyk.system.iesaschool.business.impl.integration;

import lombok.Setter;

public class PopulateWithFakedDataBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
 
    @Setter private Integer numbreOfTeachers = 10;
	@Setter private Integer numbreOfStudents = 50;
	@Setter private Integer numbreOfStudentsByClassroomSession = 3;
	
	@Setter private Boolean generateCompleteAcademicSession = Boolean.TRUE;
	@Setter private Boolean generateStudentClassroomSessionDivisionReport = Boolean.TRUE;
    
    @Override
    protected void businesses() {
    	installApplication();
    	/*
    	ExecutorService executor = Executors.newFixedThreadPool(5);
		Collection<StudentSubject> studentSubjects = new ArrayList<>();
		for(ClassroomSessionInfos classroomSessionInfos : new ClassroomSessionInfos[]{grade1,grade2,grade3}){
			Collection<Student> students = studentBusiness.findManyRandomly(numbreOfStudentsByClassroomSession);
			createStudentClassroomSessions(classroomSessionInfos, students);	
			//executor.execute(new ClassroomsessionBusinessProducer(classroomSessionInfos, listener, students,studentSubjects));
		}
		//executor.shutdown();
        //while (!executor.isTerminated()) {}
		
		//flush(StudentSubject.class,studentSubjectBusiness,studentSubjects);
		
		Collection<SubjectEvaluation> subjectEvaluations = new ArrayList<>();
		for(SubjectEvaluationType subjectEvaluationType : subjectEvaluationTypeBusiness.findAll()){
			SubjectEvaluation subjectEvaluation = new SubjectEvaluation(subjectEvaluationType, Boolean.FALSE);
			for(StudentSubject studentSubject :studentSubjectDao.readByClassroomSessionDivisionSubject(subjectEvaluationType.getSubject()) ){
				StudentSubjectEvaluation studentSubjectEvaluation = new StudentSubjectEvaluation(subjectEvaluation, studentSubject
						, new BigDecimal(RandomDataProvider.getInstance().randomInt(0, subjectEvaluationType.getMaximumValue().intValue())));
				subjectEvaluation.getStudentSubjectEvaluations().add(studentSubjectEvaluation);
			}
			subjectEvaluations.add(subjectEvaluation);
			flush(SubjectEvaluation.class,subjectEvaluationBusiness,subjectEvaluations,10000l);
		}
		flush(SubjectEvaluation.class,subjectEvaluationBusiness,subjectEvaluations);
		

		if(Boolean.TRUE.equals(generateStudentClassroomSessionDivisionReport)){
			System.out.println("Updating metric value");
			ClassroomSessionInfos classroomSessionInfos = grade1;
			SchoolBusinessTestHelper.getInstance().randomValues(Arrays.asList(classroomSessionInfos.division(0).getClassroomSessionDivision()), Boolean.TRUE, Boolean.TRUE,Boolean.TRUE);
			
			System.out.println("Generating report");
			inject(Instance().getStudentClassroomSessionDivisionBusiness.class).buildReport(Arrays.asList(classroomSessionInfos.division(0).getClassroomSessionDivision()));
		}
    	*/
    	
    }
    
}
