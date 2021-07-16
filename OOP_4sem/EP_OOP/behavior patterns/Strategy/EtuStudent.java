package Strategy;

public class EtuStudent {
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void executeActivity(){
        activity.doingSomething();
    }
}
