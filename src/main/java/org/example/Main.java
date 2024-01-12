package org.example;

public class Main {
    public static void main(String[] args) throws Exception {
        DemoApplicationContext applicationContext = new DemoApplicationContext();
        applicationContext.refresh();
        TestA testA = applicationContext.getBean("org.example.TestA", TestA.class);
        TestB testB = applicationContext.getBean("org.example.TestB", TestB.class);
        TestC testC = applicationContext.getBean("org.example.TestC", TestC.class);
        ProcessorB processorB = applicationContext.getBean("org.example.ProcessorB", ProcessorB.class);
        ProcessorA processorA = applicationContext.getBean("org.example.ProcessorA", ProcessorA.class);
        System.out.println();
    }
}