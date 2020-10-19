package contest;

import template.io.FastInput;
import java.io.PrintWriter;

public class APlusMinus {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int a = in.readInt();
        int b = in.readInt();

        int x = (a + b) / 2;
        int y = a - x;
        out.println(x);
        out.println(y);
    }
}
