package chelper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExternalExecutor {
    String path;

    public ExternalExecutor(String path) {
        this.path = path;
    }

    public String invoke(String input) {
        try {
            Process process = Runtime.getRuntime().exec(path);
            process.getOutputStream().write(input.getBytes());
            process.getOutputStream().flush();
            String output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
