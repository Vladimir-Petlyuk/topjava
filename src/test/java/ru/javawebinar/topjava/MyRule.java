package ru.javawebinar.topjava;

import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class MyRule extends TestWatcher {

    public MyRule(TestName name) {
        this.name = name;
    }

    private TestName name;
    private Long startTime;
    private Long endTime;

    @Override
    protected void starting(Description description) {
         startTime = System.currentTimeMillis();
    }

    @Override
    protected void finished(Description description) {
        endTime = System.currentTimeMillis();
        System.out.println(name.getMethodName() +" - "+(double)(endTime-startTime)/1000 + " c");
    }
}
