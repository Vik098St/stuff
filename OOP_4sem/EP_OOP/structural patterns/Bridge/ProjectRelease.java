package Bridge;

public class ProjectRelease {
    public static void main(String[] args) {
        Program [] programs = {
                new ReleaseForLinux(new AdminInterface()),
                new ReleaseForWindows(new UserInterface())
        };

        for (Program p: programs) {
            p.programGui();
        }
    }
}
