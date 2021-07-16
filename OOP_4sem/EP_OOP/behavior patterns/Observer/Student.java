package Observer;

import java.util.List;

public class Student implements Observer{
    String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void handleEvent(List<String> vacancies) {
        System.out.println(" \n Dear," + name + "\n ETU have new vacancies for you:\n"
        + vacancies + "\n ==================" );
    }
}
