package Bridge;

public class AdminInterface implements GUI{
    @Override
    public void showWindowWithButtons() {
        System.out.println("Hello Admin!This interface only for Linux");
    }
}
