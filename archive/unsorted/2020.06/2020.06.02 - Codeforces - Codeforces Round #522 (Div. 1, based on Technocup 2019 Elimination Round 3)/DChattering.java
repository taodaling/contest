package contest;


import template.io.FastInput;
import template.io.FastOutput;
import template.utils.ArrayIndex;
import template.utils.Debug;

public class DChattering {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] w = new int[n];
        in.populate(w);

        int[] left = new int[3 * n];
        int[] right = new int[3 * n];
        for (int i = 0; i < n * 3; i++) {
            left[i] = Math.max(i - w[i % n], 0);
            right[i] = Math.min(i + w[i % n], 3 * n - 1);
        }

        debug.debug("left", left);
        debug.debug("right", right);
        IntegerRMQ[][] rmq = new IntegerRMQ[15][2];
        int[][] ljump = new int[21][3 * n];
        int[][] rjump = new int[21][3 * n];
        for (int i = 0; i < n * 3; i++) {
            ljump[0][i] = left[i];
            rjump[0][i] = right[i];
        }
        rmq[0][0] = new IntegerRMQ(i -> ljump[0][i], 3 * n, Integer::compare);
        rmq[0][1] = new IntegerRMQ(i -> rjump[0][i], 3 * n, (a, b) -> -Integer.compare(a, b));

        for (int i = 0; i + 1 <= 14; i++) {
            for (int j = 0; j < 3 * n; j++) {
                int l = ljump[i][j];
                int r = rjump[i][j];
                ljump[i + 1][j] = ljump[i][rmq[i][0].query(l, r)];
                rjump[i + 1][j] = rjump[i][rmq[i][1].query(l, r)];
            }

//            for (int j = 0; j < 3 * n; j++) {
//                for (int k = 0; k < 3 * n; k++) {
//                    if (left[j] < left[k] && jump[ai.indexOf(j, i + 1, 0)] > jump[ai.indexOf(k, i + 1, 0)]) {
//                        throw new RuntimeException();
//                    }
//                    if (right[j] < right[k] && jump[ai.indexOf(j, i + 1, 1)] > jump[ai.indexOf(k, i + 1, 1)]) {
//                        throw new RuntimeException();
//                    }
//                }
//            }

            int finalI = i;
            rmq[i + 1][0] = new IntegerRMQ(j -> ljump[finalI + 1][j], 3 * n, Integer::compare);
            rmq[i + 1][1] = new IntegerRMQ(j -> rjump[finalI + 1][j], 3 * n, (a, b) -> -Integer.compare(a, b));
        }

        for (int i = n; i < 2 * n; i++) {
            int l = i;
            int r = i;
            int time = 0;
            for (int j = 14; j >= 0; j--) {

                if (rjump[j][rmq[j][1].query(l, r)] - ljump[j][rmq[j][0].query(l, r)] + 1 < n) {
                    int oldL = l;
                    int oldR = r;
                    l = ljump[j][rmq[j][0].query(oldL, oldR)];
                    r = rjump[j][rmq[j][1].query(oldL, oldR)];
                    //     r = rightIST.query(oldL, oldR);
                    //   l = leftIST.query(oldL, oldR);
                    time += 1 << j;
                }
            }
            if (r - l + 1 < n) {
                time++;
            }
            out.println(time);
        }
    }
}
