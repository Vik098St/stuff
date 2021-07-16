package Bridge;

public class ReleaseForWindows extends Program{

    protected ReleaseForWindows(GUI someGUI) {
        super(someGUI);
    }

    @Override
    public void programGui() {
        System.out.println("unique interface for Windows is started...");
        gui.showWindowWithButtons();
    }
}
