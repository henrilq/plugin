import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import org.apache.log4j.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Henri on 23/05/2017.
 */
public class ProcessManager {

    private ProcessManager() {
    }

    public static void execute(String cmd, ProcessListener listener) {
        listenProcess(createProcess(cmd), listener);
    }

    public static void execute(GeneralCommandLine generalCommandLine, ProcessListener listener) {
        listenProcess(createProcess(generalCommandLine), listener);
    }

    public static Process createProcess(String cmd) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return process;
    }

    public static Process createProcess(GeneralCommandLine generalCommandLine) {
        Process process = null;
        try {
            process = generalCommandLine.createProcess();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return process;
    }

    private static void listenProcess(final Process process, final ProcessListener listener) {
        if (process != null && listener != null) {
            Thread thread = new Thread(() -> {
                try {
                    readStream(process.getInputStream(), listener, Level.INFO);
                    readStream(process.getErrorStream(), listener, Level.ERROR);
                    listener.onFinish(process);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

    private static void readStream(InputStream stream, ProcessListener listener, Level level) throws IOException {
        InputStreamReader in = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(in);
        String line;
        while ((line = reader.readLine()) != null) {
            listener.onRun(level ,line);
        }
    }
}
