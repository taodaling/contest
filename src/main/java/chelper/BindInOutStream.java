package chelper;

import java.io.*;

public class BindInOutStream implements Runnable {
    BufferedReader bis;
    BufferedWriter bos;

    public BindInOutStream(InputStream is, OutputStream os) {
        bis = new BufferedReader(new InputStreamReader(is));
        bos = new BufferedWriter(new OutputStreamWriter(os));
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = bis.readLine()) != null) {
                bos.append(line).append('\n').flush();
            }
        } catch (IOException e) {
        }
    }
}
