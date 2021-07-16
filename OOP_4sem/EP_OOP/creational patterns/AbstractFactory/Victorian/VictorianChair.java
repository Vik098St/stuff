public class VictorianChair implements Chair{
    @Override
    public void sitOn(){
        System.out.println("Looks old, but sit in - gold!");
    }

    @Override
    public void hasLags() {
        System.out.println("has 4 small lags");
    }
}
