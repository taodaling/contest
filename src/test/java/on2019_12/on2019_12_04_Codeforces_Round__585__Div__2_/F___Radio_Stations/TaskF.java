package on2019_12.on2019_12_04_Codeforces_Round__585__Div__2_.F___Radio_Stations;



import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskF {
    int p;
    int M;

    public int idOfStation(int x) {
        return x - 1;
    }

    public int idOfGE(int x) {
        return p + x - 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        p = in.readInt();
        M = in.readInt();
        int m = in.readInt();

        TwoSatBeta ts = new TwoSatBeta(p + M + 1, (n + M + p + m));
        for (int i = 0; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            ts.atLeastOneIsTrue(ts.elementId(idOfStation(a)), ts.elementId(idOfStation(b)));
        }

        for (int i = 2; i <= M + 1; i++) {
            ts.deduce(ts.elementId(idOfGE(i)), ts.elementId(idOfGE(i - 1)));
        }

        for (int i = 1; i <= p; i++) {
            int l = in.readInt();
            int r = in.readInt();
            ts.deduce(ts.elementId(idOfStation(i)),
                    ts.elementId(idOfGE(l)));
            ts.deduce(ts.elementId(idOfStation(i)),
                    ts.negateElementId(idOfGE(r + 1)));
        }

        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            ts.atLeastOneIsFalse(ts.elementId(idOfStation(a)), ts.elementId(idOfStation(b)));
        }

        boolean solvable = ts.solve(true);
        if (!solvable) {
            out.println(-1);
            return;
        }
        int k = 0;
        int f = 0;
        for (int i = 1; i <= p; i++) {
            if (ts.valueOf(idOfStation(i))) {
                k++;
            }
        }
        for (int i = 1; i <= M + 1; i++) {
            if (ts.valueOf(idOfGE(i))) {
                f = i;
            }
        }
        out.append(k).append(' ').append(f).println();
        for (int i = 1; i <= p; i++) {
            if (ts.valueOf(idOfStation(i))) {
                out.append(i).append(' ');
            }
        }
    }

}