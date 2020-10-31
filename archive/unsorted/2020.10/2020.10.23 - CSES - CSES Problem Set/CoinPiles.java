package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class CoinPiles {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        long a = in.readLong();
        long b = in.readLong();
        //2x+y=a
        //x+2y=b
        //3x=2a-b
        //3y=2b-a
        if ((2 * a - b) >= 0 && (2 * a - b) % 3 == 0 &&
                (2 * b - a) >= 0 && (2 * b - a) % 3 == 0) {
            out.println("YES");
        }else{
            out.println("NO");
        }
    }
}
