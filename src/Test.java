import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Henri on 10/04/2017.
 */
public class Test {

    static Set<String> pidList = new HashSet<>();
    static Set<Process> processList = new HashSet<>();

    public static void main(String[] args) throws InterruptedException {
        File file = new File("C:/Users/Henri/Documents/Fax/");
        loadConfiguration(file);
        ProcessListener listener = new ProcessListener() {
            @Override
            public void onRun(Level level, String res) {
                if (Level.ERROR.equals(level)) {
                    Logger.getLogger(Test.class).error(res);
                } else {
                    Logger.getLogger(Test.class).info(res);
                }
            }

            @Override
            protected void onFinish(Process process) {
                System.out.println("finish");
                //killConhostPID();
                for (Process pr: processList) {
                    if(pr.isAlive()){
                        pr.destroy();
                    }
                }
                System.out.println("exit value" +process.exitValue());

                /*
                ProcessBuilderFactory.killProcessByName().addParameters("cmd.exe").run();
                ProcessBuilderFactory.killProcessByName().addParameters("firefox.exe").run();
                ProcessBuilderFactory.killProcessByName().addParameters("vlc.exe").run();
                ProcessBuilderFactory.killProcessByName().addParameters("ping.exe").run();
                */

            }
        };
        processList.add(ProcessBuilderFactory.startFireFox().listen(listener).run());
        processList.add(ProcessBuilderFactory.startVLC().listen(listener).run());
        processList.add(ProcessBuilderFactory.startTest().listen(listener)
                .addParameters("Henri", "LEQUINTREC").run());
        saveConhostPID();
    }

    private static void saveConhostPID() {
        ProcessListener listener = new ProcessListener() {
            @Override
            public void onRun(Level level, String res) {
                if (Level.INFO.equals(level)) {
                    String[] tab = res.split("\\s");
                    for (String s : tab) {
                        if (!s.isEmpty() && s.matches("[0-9]*")) {
                            pidList.add(s);
                        }
                    }
                }
            }
        };
        ProcessBuilder.cmd("tasklist")
                .addParameters("/svc")
                .addParameters("/fi")
                .addParameters("imagename eq conhost.exe")
                .listen(listener)
                .run();
    }

    private static void killConhostPID() {
        Iterator<String> iter = pidList.iterator();
        while (iter.hasNext()) {
            String pid = iter.next();
            ProcessBuilderFactory.killProcessByPid().addParameters(pid).run();
            iter.remove();
        }
    }

    private static void loadConfiguration(File directory) {
        File logConfFile = new File(directory.getAbsolutePath()+File.separator+"log4j.properties");
        Properties props = null;
        if(logConfFile.exists()){
            props = loadFileConfiguration(logConfFile);
        }else{
            props = createDefaultConfiguration(directory);
            OutputStream out = null;
            try {
                out = new FileOutputStream(logConfFile);
                props.store(out, "");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }

    private static Properties loadFileConfiguration(File logConfFile) {
        Properties props = null;
        InputStream in = null;
        try {
            in = new FileInputStream(logConfFile);
            props = new Properties();
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    private static Properties createDefaultConfiguration(File directory) {
        Properties props = new Properties();
        props.put("log4j.rootLogger", "debug, stdout, R");
        props.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        props.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");

        props.put("log4j.appender.stdout.layout.ConversionPattern", "%p - %m%n");
        props.put("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
        props.put("log4j.appender.R.File", directory.getAbsolutePath()+File.separator+"log.txt");

        props.put("log4j.appender.R.MaxFileSize", "100KB");
        props.put("log4j.appender.R.MaxBackupIndex", "4");

        props.put("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
        props.put("log4j.appender.R.layout.ConversionPattern", "%p %d %t - %m%n");
        return props;
    }
}
