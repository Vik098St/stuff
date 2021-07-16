package Observer;

public interface Observed {
    public void addObserver(Observer observer);
    public void notifyObservers();
    public void removeObserver(Observer observer);
}
