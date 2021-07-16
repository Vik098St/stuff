package State;

public class EtuStudent {
    private  Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void changeActivity(){
        if(activity instanceof Sleeping){setActivity(new Eating());}
        else if(activity instanceof Eating){setActivity(new Study());}
        else if(activity instanceof Study){setActivity(new Sleeping());}
    }

    public void doingSomething(){
        activity.doingSomething();
    }
}
