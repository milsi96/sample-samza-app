package task;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.AsyncStreamTask;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.TaskCallback;
import org.apache.samza.task.TaskCoordinator;

public class SampleAsyncTask implements AsyncStreamTask {
    @Override
    public void processAsync(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator, TaskCallback callback) {
        collector.send(new OutgoingMessageEnvelope(new SystemStream("kafka", "results"), envelope.toString()));
        callback.complete();
        System.out.println("Async task");
    }
}
