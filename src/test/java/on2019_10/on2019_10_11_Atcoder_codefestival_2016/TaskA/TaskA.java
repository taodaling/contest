package on2019_10.on2019_10_11_Atcoder_codefestival_2016.TaskA;



import template.FastInput;
import java.io.PrintWriter;

public class TaskA {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        String s = in.readString();
        s = s.substring(0, 4) + " " + s.substring(4);
        out.println(s);
    }
}
