package com.al.po.tuning;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Threads(Threads.MAX)
public class SingleShotTimeTest {


    int a;

    @Benchmark
    @Group("a")
    public void test() {
        try {
            System.out.println("plus before, a = " + a);
            a++;
            System.out.println("plus after before sleep, a = " + a + " Thread - " + Thread.currentThread().getName());
            sleep(1000);
            System.out.println("a = " + a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @TearDown
    public void aTest(){
        System.out.println("============="+a);
    }

    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(SingleShotTimeTest.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opts).run();
    }
}
