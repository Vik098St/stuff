package Bridge;

public class ReleaseForLinux extends Program{
    protected ReleaseForLinux(GUI someGUI) {
        super(someGUI);
    }

    @Override
    public void programGui() {
        System.out.println("unique interface for Linux is started...");
        gui.showWindowWithButtons();
    }
}
