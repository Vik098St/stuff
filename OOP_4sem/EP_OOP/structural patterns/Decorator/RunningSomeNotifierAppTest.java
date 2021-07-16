package Decorator;

public class RunningSomeNotifierAppTest {
    public static void main(String[] args) {
        Notifier notifier = new SuperAdvanceNotifierApp( new AdvanceNotifierApp(new SomeAppWithNotifier()));
        System.out.println(notifier.sendMessage());
    }
}
