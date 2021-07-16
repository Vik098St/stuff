public class ModernSofa implements Sofa{
    @Override
    public void sleepIn() {
        System.out.println("Looks great! But is it comfortable?");
    }

    @Override
    public void isComfortable() {
        System.out.println("It is so hard to the touch");
    }
}
