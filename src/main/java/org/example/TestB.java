package org.example;

@Logger
@Component
public class TestB {

    @Autowired
    private TestA testA;

    @Autowired
    private TestC testC;
}
