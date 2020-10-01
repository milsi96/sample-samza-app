package task;

import org.apache.samza.task.AsyncStreamTaskFactory;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.StreamTaskFactory;

public class SampleTaskFactory implements StreamTaskFactory {
    @Override
    public StreamTask createInstance() {
        return new SampleStreamTask();
    }
}
