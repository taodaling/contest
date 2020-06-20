package on2020_06.on2020_06_02_Codeforces___Codeforces_Round__522__Div__1__based_on_Technocup_2019_Elimination_Round_3_.D__Chattering;




import template.datastructure.RMQ;
import template.datastructure.RMQBySegment;
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
        RMQ model = new RMQ(3 * n);
        RMQ[][] rmq = new RMQ[15][2];
        int[][] ljump = new int[15][3 * n];
        int[][] rjump = new int[15][3 * n];
        for (int i = 0; i < n * 3; i++) {
            ljump[0][i] = left[i];
            rjump[0][i] = right[i];
        }
        rmq[0][0] = new RMQ(model);
        rmq[0][1] = new RMQ(model);
        rmq[0][0].reset(0, 3 * n - 1, (x, y) -> Integer.compare(ljump[0][x], ljump[0][y]));
        rmq[0][1].reset(0, 3 * n - 1, (x, y) -> -Integer.compare(rjump[0][x], rjump[0][y]));

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

            int finalI = i + 1;
            rmq[finalI][0] = new RMQ(model);
            rmq[finalI][1] = new RMQ(model);
            rmq[finalI][0].reset(0, 3 * n - 1, (x, y) -> Integer.compare(ljump[finalI][x], ljump[finalI][y]));
            rmq[finalI][1].reset(0, 3 * n - 1, (x, y) -> -Integer.compare(rjump[finalI][x], rjump[finalI][y]));
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
