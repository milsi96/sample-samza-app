import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import joptsimple.OptionSet;
import org.apache.samza.application.TaskApplication;
import org.apache.samza.application.descriptors.TaskApplicationDescriptor;
import org.apache.samza.config.Config;
import org.apache.samza.operators.KV;
import org.apache.samza.runtime.LocalApplicationRunner;
import org.apache.samza.serializers.KVSerde;
import org.apache.samza.serializers.StringSerde;
import org.apache.samza.system.kafka.descriptors.KafkaInputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaOutputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaSystemDescriptor;
import org.apache.samza.util.CommandLine;
import task.SampleAsyncTaskFactory;
import task.SampleTaskFactory;

import java.util.List;
import java.util.Map;

public class SampleTaskApplication implements TaskApplication {
    private static final String KAFKA_SYSTEM_NAME = "kafka";
    private static final List<String> KAFKA_CONSUMER_ZK_CONNECT = ImmutableList.of("localhost:2181");
    private static final List<String> KAFKA_PRODUCER_BOOTSTRAP_SERVERS = ImmutableList.of("localhost:9092");
    private static final Map<String, String> KAFKA_DEFAULT_STREAM_CONFIGS = ImmutableMap.of("replication.factor", "1");

    private static final String INPUT_STREAM_ID = "events";
    private static final String OUTPUT_STREAM_ID = "results";

    @Override
    public void describe(TaskApplicationDescriptor appDescriptor) {
        KafkaSystemDescriptor kafkaSystemDescriptor = new KafkaSystemDescriptor(KAFKA_SYSTEM_NAME)
                .withConsumerZkConnect(KAFKA_CONSUMER_ZK_CONNECT)
                .withProducerBootstrapServers(KAFKA_PRODUCER_BOOTSTRAP_SERVERS)
                .withDefaultStreamConfigs(KAFKA_DEFAULT_STREAM_CONFIGS);

        KafkaInputDescriptor<KV<String, String>> inputDescriptor =
                kafkaSystemDescriptor.getInputDescriptor(INPUT_STREAM_ID,
                        KVSerde.of(new StringSerde(), new StringSerde()));
        KafkaOutputDescriptor<KV<String, String>> outputDescriptor =
                kafkaSystemDescriptor.getOutputDescriptor(OUTPUT_STREAM_ID,
                        KVSerde.of(new StringSerde(), new StringSerde()));

        appDescriptor
                .withInputStream(inputDescriptor)
                .withOutputStream(outputDescriptor);

        appDescriptor.withTaskFactory(new SampleAsyncTaskFactory());
    }

    public static void main(String[] args) {
        CommandLine cmdLine = new CommandLine();
        OptionSet options = cmdLine.parser().parse(args);
        Config config = cmdLine.loadConfig(options);
        LocalApplicationRunner runner = new LocalApplicationRunner(new SampleTaskApplication(), config);
        runner.run();
        runner.waitForFinish();
    }
}
