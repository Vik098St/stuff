package Bridge;

public abstract class Program {
    protected GUI gui;

    protected Program(GUI someGUI){
        this.gui = someGUI;
    }

    public abstract void programGui();
}
