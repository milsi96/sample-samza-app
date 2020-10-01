package task;

import org.apache.samza.task.AsyncStreamTask;
import org.apache.samza.task.AsyncStreamTaskFactory;

public class SampleAsyncTaskFactory implements AsyncStreamTaskFactory {
    @Override
    public AsyncStreamTask createInstance() {
        return new SampleAsyncTask();
    }
}
