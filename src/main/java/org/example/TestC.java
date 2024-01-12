package org.example;

@Component
public class TestC {

    @Autowired
    private TestA testA;

    @Autowired
    private ProcessorB processorB;

    @Autowired
    private TestB testB;
}
