package chelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class ExternalInterator {
    Process process;

    public ExternalInterator(String[] cmds) throws IOException {
        process = new ProcessBuilder(cmds).redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();
    }

    public InputStream getIn() {
        return process.getInputStream();
    }

    public OutputStream getOut() {
        return process.getOutputStream();
    }

    public int getExitCode() throws InterruptedException {
        if (process.waitFor(10, TimeUnit.SECONDS)) {
            return process.exitValue();
        } else {
            process.destroyForcibly();
            return -1;
        }
    }
}
