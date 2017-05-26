import com.intellij.execution.configurations.GeneralCommandLine;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Henri on 25/05/2017.
 */
public class ProcessBuilder {
    private String command;
    private String exePath;
    private Map<String, String> env;
    private List<String[]> parameters;
    private ProcessListener processListener;

    private ProcessBuilder(@NotNull String command){
        this.command = command;
        this.env = new LinkedHashMap<>();
        parameters = new ArrayList<>();
    }

    public static ProcessBuilder cmd(@NotNull String command){
        ProcessBuilder builder = new ProcessBuilder(command);
        return builder;
    }

    public ProcessBuilder addParameters(String ... param){
        parameters.add(param);
        return this;
    }

    public ProcessBuilder listen(ProcessListener listener){
        this.processListener = listener;
        return this;
    }

    public ProcessBuilder setExePath(String exePath) {
        this.exePath = exePath;
        return this;
    }

    public ProcessBuilder addEnv(String key, String value){
        env.put(key, value);
        return this;
    }

    public Process run(){
        Process process = null;
        try {
            GeneralCommandLine cmd = new GeneralCommandLine(command);
            for (String[] params: parameters) {
                cmd.addParameters(params);
            }
            if(exePath != null){
                cmd.setExePath(exePath);
            }
            if(! env.isEmpty()){
                cmd.getEnvironment().putAll(env);
            }
            Logger.getLogger(ProcessBuilder.class).debug(cmd.getCommandLineString());
            process = cmd.createProcess();
            listenProcess(process, processListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return process;
    }

    private void listenProcess(final Process process, final ProcessListener listener) {
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

    private void readStream(InputStream stream, ProcessListener listener, Level level) throws IOException {
        InputStreamReader in = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(in);
        String line;
        while ((line = reader.readLine()) != null) {
            listener.onRun(level ,line);
        }
    }

}
