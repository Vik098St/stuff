public class ModernChair implements Chair{
    @Override
    public void sitOn(){
        System.out.println("Not comfortable but futuristic!");
    }

    @Override
    public void hasLags() {
        System.out.println("Look! This chair has no lags!");
    }
}
