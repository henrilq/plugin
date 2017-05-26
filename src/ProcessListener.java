import org.apache.log4j.Level;

/**
 * Created by Henri on 23/05/2017.
 */
public abstract class ProcessListener {

    public abstract void onRun(Level level, String res);

    protected void onFinish(Process process){

    }
}
