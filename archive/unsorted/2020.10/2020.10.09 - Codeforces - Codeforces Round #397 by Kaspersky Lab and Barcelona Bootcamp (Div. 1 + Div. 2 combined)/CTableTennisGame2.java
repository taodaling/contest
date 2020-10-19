package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class CTableTennisGame2 {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int k = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        if (a < k && b < k || a < k && b % k != 0 || b < k && a % k != 0) {
            out.println(-1);
            return;
        }
        out.println(a / k + b / k);
    }
}
