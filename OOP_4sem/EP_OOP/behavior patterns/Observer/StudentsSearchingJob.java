package Observer;

public class StudentsSearchingJob {
    public static void main(String[] args) {
        JobForStudentsSite site = new JobForStudentsSite();
        site.addVacancy("java developer");

        Observer student1 = new Student("Stepovik Viktor");
        site.addObserver(student1);
        Observer student2 = new Student("Roman Pavlov");
        site.addObserver(student2);


        site.addVacancy("cpp developer");
        site.addVacancy("DevOps");

        site.removeVacancy("cpp developer");
    }
}
