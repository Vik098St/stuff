package State;

public class EtuStudentDay {
    public static void main(String[] args) {
        Activity activity = new Sleeping();
        EtuStudent student = new EtuStudent();

        student.setActivity(activity);
        for (int i = 0; i < 7; i++) {
            student.doingSomething();
            student.changeActivity();
        }
    }
}
