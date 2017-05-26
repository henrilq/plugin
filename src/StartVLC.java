/**
 * Created by Henri on 24/05/2017.
 */
public class StartVLC implements ProcessMultiOS {
    @Override
    public ProcessBuilder onWindows() {
        return ProcessBuilder.cmd("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe");
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
