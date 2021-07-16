package Proxy;

public class ProxyDatabaseConnector implements Database {
    private String query;
    private MyDatabase myProject;

    public ProxyDatabaseConnector(String query) {
        this.query = query;
    }
    /** если проект пуст то при попытке его запустить программа сначла скачает его**/
    @Override
    public void connect() {
        if(myProject == null)
            myProject = new MyDatabase(query);
        myProject.connect();
    }
}
