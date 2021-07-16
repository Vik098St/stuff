package Observer;

import java.util.ArrayList;
import java.util.List;

public class JobForStudentsSite implements Observed{
    List<String> vacancies = new ArrayList<>();
    List<Observer> students = new ArrayList<>();

    public void addVacancy(String vacancy){
        this.vacancies.add(vacancy);
        notifyObservers();
    }

    public void removeVacancy(String vacancy){
        this.vacancies.remove(vacancy);
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        this.students.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o: students) {
            o.handleEvent(this.vacancies);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        this.students.remove(observer);
    }
}
