import org.jdesktop.swingx.util.OS;

/**
 * Created by Henri on 25/05/2017.
 */
public class ProcessBuilderFactory {

    public static ProcessBuilder startVLC() {
        return selectOS(new StartVLC());
    }

    public static ProcessBuilder startFireFox() {
        return selectOS(new StartFirefox());
    }

    public static ProcessBuilder startTest() {
        return ProcessBuilder.cmd("C:\\Users\\Henri\\Desktop\\test.bat");
    }



    public static ProcessBuilder killProcessByName() {
        return selectOS(new KillProcessByName());
    }

    public static ProcessBuilder killProcessByPid() {
        return selectOS(new KillProcessByPid());
    }

    private static ProcessBuilder selectOS(ProcessMultiOS processMultiOS) {
        ProcessBuilder processBuilder = null;
        if (OS.isLinux()) {
            processBuilder = processMultiOS.onLinux();
        } else if (OS.isWindows()) {
            processBuilder = processMultiOS.onWindows();
        } else if (OS.isMacOSX()) {
            processBuilder = processMultiOS.onMacOS();
        }
        return processBuilder;
    }
}
