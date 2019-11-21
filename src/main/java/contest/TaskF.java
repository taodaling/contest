package contest;


import template.algo.TwoSat;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int M = in.readInt();
        int m = in.readInt();

        TwoSat twoSat = new TwoSat(p + M + 1);

        for (int i = 1; i <= M; i++) {
            twoSat.deduce(twoSat.getElement(p + i + 1), twoSat.getElement(p + i));
        }

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            twoSat.or(twoSat.getElement(x), twoSat.getElement(y));
        }

        for (int i = 1; i <= p; i++) {
            int l = in.readInt();
            int r = in.readInt();
            twoSat.deduce(twoSat.getElement(i), twoSat.getElement(p + l));
            twoSat.deduce(twoSat.getElement(i), twoSat.getNotElement(p + r + 1));
        }

        for (int i = 1; i <= m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            twoSat.atLeastOneIsFalse(twoSat.getElement(u), twoSat.getElement(v));
        }

        if (!twoSat.solve(true)) {
            out.append(-1);
            return;
        }
        int k = 0;
        int f = 0;
        for (int i = 1; i <= p; i++) {
            if (twoSat.valueOf(i)) {
                k++;
            }
        }

        for (int i = 1; i <= M; i++) {
            if (twoSat.valueOf(p + i) && !twoSat.valueOf(p + i + 1)) {
                f = i;
                break;
            }
        }

        out.append(k).append(' ').append(f).append('\n');
        for (int i = 1; i <= p; i++) {
            if (twoSat.valueOf(i)) {
                out.append(i).append(' ');
            }
        }
    }
}
