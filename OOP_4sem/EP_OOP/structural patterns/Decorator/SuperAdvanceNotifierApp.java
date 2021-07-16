package Decorator;

public class SuperAdvanceNotifierApp extends NotifierDecorator{
    public SuperAdvanceNotifierApp(Notifier notifier) {
        super(notifier);
    }

    public String sendQuestion(){
        return "\nYou look great today! would you like to buy some new products ?";
    }

    public String sendWeekReport(){
        return "\nHere you can see the report for this week.";
    }

    @Override
    public String sendMessage() {
        return super.sendMessage() + sendWeekReport()+ sendQuestion();
    }
}
