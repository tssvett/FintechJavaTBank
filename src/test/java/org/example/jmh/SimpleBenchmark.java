package org.example.jmh;

import java.util.concurrent.TimeUnit;
import org.example.task14.enums.QueueType;
import org.example.task14.interfaces.consumer.QueueConsumer;
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
    private QueueConfiguration simpleRabbitConfig;
    private QueueConfiguration loadBalancingRabbitConfig;
    private QueueConfiguration multipleConsumersRabbitConfig;
    private QueueConfiguration loadBalancingAndMultipleConsumersRabbitConfig;
    private QueueConfiguration stressTestRabbitConfig;

    private QueueConfiguration simpleKafkaConfig;
    private QueueConfiguration loadBalancingKafkaConfig;
    private QueueConfiguration multipleConsumersKafkaConfig;
    private QueueConfiguration loadBalancingAndMultipleConsumersKafkaConfig;
    private QueueConfiguration stressTestKafkaConfig;
    ConfigurationsCreator configCreator;


    @Setup(Level.Trial)
    public void setup() {
        configCreator = new ConfigurationsCreator();
        simpleRabbitConfig = configCreator.createConfiguration(1, 1, QueueType.RABBITMQ);
        loadBalancingRabbitConfig = configCreator.createConfiguration(3, 1, QueueType.RABBITMQ);
        multipleConsumersRabbitConfig = configCreator.createConfiguration(1, 3, QueueType.RABBITMQ);
        loadBalancingAndMultipleConsumersRabbitConfig = configCreator.createConfiguration(3, 3, QueueType.RABBITMQ);
        stressTestRabbitConfig = configCreator.createConfiguration(10, 10, QueueType.RABBITMQ);

        simpleKafkaConfig = configCreator.createConfiguration(1, 1, QueueType.KAFKA);
        loadBalancingKafkaConfig = configCreator.createConfiguration(3, 1, QueueType.KAFKA);
        multipleConsumersKafkaConfig = configCreator.createConfiguration(1, 3, QueueType.KAFKA);
        loadBalancingAndMultipleConsumersKafkaConfig = configCreator.createConfiguration(3, 3, QueueType.KAFKA);
        stressTestKafkaConfig = configCreator.createConfiguration(10, 10, QueueType.KAFKA);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        configCreator.clear();
        simpleRabbitConfig.stop();
        loadBalancingRabbitConfig.stop();
        multipleConsumersRabbitConfig.stop();
        loadBalancingAndMultipleConsumersRabbitConfig.stop();
        stressTestRabbitConfig.stop();
        simpleKafkaConfig.stop();
        loadBalancingKafkaConfig.stop();
        multipleConsumersKafkaConfig.stop();
        loadBalancingAndMultipleConsumersKafkaConfig.stop();
        stressTestKafkaConfig.stop();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void simpleRabbitProducerLatency() {
        simpleRabbitConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void simpleRabbitConsumerLatency() {
        simpleRabbitConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void simpleRabbitThroughput() {
        simpleRabbitConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingRabbitConfigProducerLatency() {
        loadBalancingRabbitConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingRabbitConfigConsumerLatency() {
        loadBalancingRabbitConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingRabbitConfigThroughput() {
        loadBalancingRabbitConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void multipleConsumersRabbitConfigProducerLatency() {
        multipleConsumersRabbitConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void multipleConsumersRabbitConfigConsumerLatency() {
        multipleConsumersRabbitConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void multipleConsumersRabbitConfigThroughput() {
        multipleConsumersRabbitConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingAndMultipleConsumersRabbitConfigProducerLatency() {
        loadBalancingAndMultipleConsumersRabbitConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingAndMultipleConsumersRabbitConfigConsumerLatency() {
        loadBalancingAndMultipleConsumersRabbitConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingAndMultipleConsumersRabbitConfigThroughput() {
        loadBalancingAndMultipleConsumersRabbitConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void stressTestRabbitConfigProducerLatency() {
        stressTestRabbitConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void stressTestRabbitConfigConsumerLatency() {
        stressTestRabbitConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void stressTestRabbitConfigThroughput() {
        stressTestRabbitConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void multipleConsumersStressTestRabbitConfigProducerLatency() {
        simpleKafkaConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void multipleConsumersStressTestRabbitConfigConsumerLatency() {
        simpleKafkaConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void multipleConsumersStressTestRabbitConfigThroughput() {
        simpleKafkaConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingKafkaConfigProducerLatency() {
        loadBalancingKafkaConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingKafkaConfigConsumerLatency() {
        loadBalancingKafkaConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingKafkaConfigThroughput() {
        loadBalancingKafkaConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingAndMultipleConsumersKafkaConfigProducerLatency() {
        loadBalancingAndMultipleConsumersKafkaConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingAndMultipleConsumersKafkaConfigConsumerLatency() {
        loadBalancingAndMultipleConsumersKafkaConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void loadBalancingAndMultipleConsumersKafkaConfigThroughput() {
        loadBalancingAndMultipleConsumersKafkaConfig.run();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void stressTestKafkaConfigConsumerLatency() {
        stressTestKafkaConfig.queueConsumerList()
                .forEach(QueueConsumer::consumeMessage);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void stressTestKafkaConfigProducerLatency() {
        stressTestKafkaConfig.queueProducerList()
                .forEach(producer -> producer.produceMessage("topic", simpleRabbitConfig.message()));
    }


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 10)
    public void stressTestKafkaConfigThroughput() {
        stressTestKafkaConfig.run();
    }


    public static void main(String[] args) throws RunnerException {
        // Configure benchmark options
        Options opt = new OptionsBuilder()
                .include(SimpleBenchmark.class.getSimpleName())
                .output("benchmark_results.txt")
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(10)
                .build();

        // Run the benchmark
        new Runner(opt).run();
    }
}