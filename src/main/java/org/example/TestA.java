package org.example;

@Logger
@Component
public class TestA {

    @Autowired
    private TestB testB;

    @Autowired
    private TestC testC;

    public void test() {
        System.out.println("打印日志：testA");
    }
}
