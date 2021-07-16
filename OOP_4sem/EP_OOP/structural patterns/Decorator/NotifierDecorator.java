package Decorator;

public class NotifierDecorator implements Notifier{

    Notifier notifier;

    public NotifierDecorator(Notifier notifier){
        this.notifier = notifier;
    }

    @Override
    public String sendMessage() {
        return notifier.sendMessage();
    }
}
