package org.example;

@Component
public class TestA {

    @Autowired
    private TestB testB;

    @Autowired
    private TestC testC;
}
