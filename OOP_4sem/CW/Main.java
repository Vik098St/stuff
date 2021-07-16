package com.company;
import com.company.ui.*;

public class Main {
    public static void main(String[] args)     {
        try {
            new UI();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Can not connect to BD");
        }
    }

}