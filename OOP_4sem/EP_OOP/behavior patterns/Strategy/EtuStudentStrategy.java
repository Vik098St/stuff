package Strategy;

public class EtuStudentStrategy {
    public static void main(String[] args) {
       /** в отличии от шаблона "состояние" **/
       /** клиентский код задаёт последовательность поведения объекта **/
        EtuStudent student = new EtuStudent();

        student.setActivity(new Study());
        student.executeActivity();

        student.setActivity(new Eating());
        student.executeActivity();

        student.setActivity(new Study());
        student.executeActivity();

        student.setActivity(new Sleeping());
        student.executeActivity();
    }
}
