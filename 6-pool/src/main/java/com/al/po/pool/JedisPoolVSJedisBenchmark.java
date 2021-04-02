package com.al.po.pool;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@Fork(2)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@BenchmarkMode(Mode.Throughput)
public class JedisPoolVSJedisBenchmark {
    JedisPool pool = new JedisPool("localhost", 6379);

    @Benchmark
    public void testPool() {
        Jedis jedis = pool.getResource();
        jedis.set("a", UUID.randomUUID().toString());
        jedis.close();
    }

    @Benchmark
    public void testJedis() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("a", UUID.randomUUID().toString());
        jedis.close();
    }

    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(JedisPoolVSJedisBenchmark.class.getSimpleName())
                .resultFormat(ResultFormatType.CSV)
                .build();

        new Runner(opts).run();
    }
}
