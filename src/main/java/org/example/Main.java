package org.example;

public class Main {
    public static void main(String[] args) throws Exception {
        DemoApplicationContext applicationContext = new DemoApplicationContext();
        applicationContext.refresh();
        TestA testA = applicationContext.getBean("org.example.TestA", TestA.class);
        System.out.println();
    }
}