package org.example.jmh;

import java.util.concurrent.TimeUnit;
import org.example.task14.queueconfiguraion.QueueConfiguration;
import org.example.task14.utils.ConfigurationsCreator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
public class SimpleBenchmark {
    private QueueConfiguration simpleRabbitConfiguration;
    ConfigurationsCreator configurationsCreator;

    @Setup(Level.Trial)
    public void setup() {
        configurationsCreator = new ConfigurationsCreator();
        simpleRabbitConfiguration = configurationsCreator.createSimpleRabbitConfiguration();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        configurationsCreator.clear();
        simpleRabbitConfiguration.stop();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void simpleRabbitAverageTime() {
        simpleRabbitConfiguration.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void simpleRabbitThroughput() {
        simpleRabbitConfiguration.run();
    }



    public static void main(String[] args) throws RunnerException {
        // Configure benchmark options
        Options opt = new OptionsBuilder()
                .include(SimpleBenchmark.class.getSimpleName())
                .output("benchmark_results.txt") // Specify output file
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(10)
                .build();

        // Run the benchmark
        new Runner(opt).run();
    }
}