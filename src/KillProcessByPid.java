/**
 * Created by Henri on 25/05/2017.
 */
public class KillProcessByPid implements ProcessMultiOS{
    @Override
    public ProcessBuilder onWindows() {
        return ProcessBuilder.cmd("taskkill")
                .addParameters("/F")
                .addParameters("/PID");
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