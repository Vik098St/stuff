package Decorator;

public class SomeAppWithNotifier implements Notifier{
    @Override
    public String sendMessage() {
        return "some text from Notifier!";
    }
}
