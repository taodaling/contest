package template.io;

import java.io.*;
import java.util.StringTokenizer;

public class InputReader {
    private StringTokenizer tokenizer = new StringTokenizer("");
    private BufferedReader br;

    public InputReader(InputStream is){
        this(new InputStreamReader(is));
    }

    public InputReader(Reader is) {
        br = new BufferedReader(is);
    }

    public String next() {
        while (!tokenizer.hasMoreElements()) {
            try {
                tokenizer = new StringTokenizer(br.readLine());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return tokenizer.nextToken();
    }

    public int nextInt(){
        return Integer.parseInt(next());
    }

    public long nextLong(){
        return Long.parseLong(next());
    }

    public double nextDouble(){
        return Double.parseDouble(next());
    }
}
