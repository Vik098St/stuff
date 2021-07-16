package Adapter;

public class Adapter extends JavaApp implements Database{
    @Override
    public void insert() {
        saveObject();
    }

    @Override
    public void select() {
        loadObject();
    }

    @Override
    public void update() {
       changeObject();
    }

    @Override
    public void remove() {
        deleteObject();
    }
}
