package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class EscapeTheTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = 1;
        this.in = in;
        this.out = out;
        while (t > 0) {
            t--;
            solve();
            int ans = in.readInt();
            if (ans == -1) {
                throw new RuntimeException();
            }
        }
    }

    FastInput in;
    FastOutput out;

    public int q1(int x) {
        out.append(1).append(' ').append(x).println().flush();
        int ans = in.readInt();
        if(ans == -1){
            throw new RuntimeException();
        }
        return ans;
    }


    public int q2(int x, int y) {
        out.append(2).append(' ').append(x).append(' ').append(y).println().flush();
        int ans = in.readInt();
        if(ans <= 0){
            throw new RuntimeException();
        }
        return ans;
    }

    public void solve() {
        int h = in.readInt();
        int node = q2(1, h);
        while (true) {
            int d = q1(node);
            if (d == 0) {
                break;
            }
            node = q2(node, d);
            if (d == 2) {
                break;
            }
        }
        out.append(3).append(' ').append(node).println().flush();
    }
}
