package Adapter;

public class DatabaseRunner {
    public static void main(String[] args) {
        Database database = new Adapter();

        database.insert();
        database.select();
        database.update();
        database.remove();
    }
}
