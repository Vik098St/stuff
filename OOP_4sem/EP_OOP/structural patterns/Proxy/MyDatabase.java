package Proxy;

public class MyDatabase implements Database {
    private String query;

    public MyDatabase(String query) {
        this.query = query;
        loadFromDB(query);
    }

    public void loadFromDB(String query){
        System.out.println("Loading data from DB with:" + query + "...");
    }

    @Override
    public void connect() {
        System.out.println("Connection received!");
    }
}
