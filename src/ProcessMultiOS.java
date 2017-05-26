/**
 * Created by Henri on 23/05/2017.
 */
public interface ProcessMultiOS {
    ProcessBuilder onWindows();

    ProcessBuilder onLinux();

    ProcessBuilder onMacOS();
}
