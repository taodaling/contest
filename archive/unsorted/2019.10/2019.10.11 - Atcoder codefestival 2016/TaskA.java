package contest;

import template.FastInput;
import java.io.PrintWriter;

public class TaskA {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        String s = in.readString();
        s = s.substring(0, 4) + " " + s.substring(4);
        out.println(s);
    }
}
