package Strategy;

public class Sleeping implements Activity {
    @Override
    public void doingSomething() {
        System.out.println("Well, I'm gonna rest now");
    }
}
