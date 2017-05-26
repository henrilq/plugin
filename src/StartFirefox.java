/**
 * Created by Henri on 23/05/2017.
 */
public class StartFirefox implements ProcessMultiOS {
    @Override
    public ProcessBuilder onWindows() {
        return ProcessBuilder.cmd("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
    }

    @Override
    public ProcessBuilder onLinux() {
        return null;
    }

    @Override
    public ProcessBuilder onMacOS() {
        return null;
    }
}
