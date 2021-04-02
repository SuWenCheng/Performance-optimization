package com.al.po.tuning;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class BenchmarkReader {
    private static final String FILE_PATH = BenchmarkReader.class.getResource("/")
            .getPath() + "nmon_analyser_v66.zip";

    //ArrayBlockingQueue
    @Benchmark
    public int bufferedReaderTest() throws Exception {
        int result = 0;
        try (Reader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            int value;
            while ((value = reader.read()) != -1) {
                result += value;
            }
        }
        return result;
    }

    @Benchmark
    public int fileReadTest() throws Exception {
        int result = 0;
        try (Reader reader = new FileReader(FILE_PATH)) {
            int value;
            while ((value = reader.read()) != -1) {
                result += value;
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(BenchmarkReader.class.getSimpleName())
                .build();

        new Runner(opts).run();
    }

}
