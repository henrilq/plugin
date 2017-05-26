/**
 * Created by Henri on 25/05/2017.
 */
public class KillProcessByName implements ProcessMultiOS{
    @Override
    public ProcessBuilder onWindows() {
        return ProcessBuilder.cmd("taskkill")
                .addParameters("/f", "/im");
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
