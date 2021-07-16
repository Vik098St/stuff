package Decorator;

public class AdvanceNotifierApp extends NotifierDecorator{

    public AdvanceNotifierApp(Notifier notifier) {
        super(notifier);
    }

    public String sendLogsAndErrors(){
        return "\n do you ever need this possibility,are you? ";
    }

    @Override
    public String sendMessage() {
        return super.sendMessage() + sendLogsAndErrors();
    }
}
