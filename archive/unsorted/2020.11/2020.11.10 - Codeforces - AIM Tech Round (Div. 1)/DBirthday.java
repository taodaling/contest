package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahamSummation;
import template.primitve.generated.datastructure.DoublePriorityQueue;
import template.primitve.generated.datastructure.IntegerPriorityQueue;
import template.utils.Debug;

public class DBirthday {
    public double success(double failP, int round) {
        return Math.log(1 - Math.pow(failP, round));
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        double[] p = new double[n];
        double[] q = new double[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() / 100d;
            q[i] = 1 - p[i];
        }

        double[] prev = new double[n];
        double[] next = new double[n];
        int[] time = new int[n];
        KahamSummation now = new KahamSummation();
        for (int i = 0; i < n; i++) {
            prev[i] = success(q[i], 1);
            now.add(prev[i]);
            next[i] = success(q[i], 2);
            time[i] = 1;
        }
        IntegerPriorityQueue pq = new IntegerPriorityQueue(n, (i, j) -> -Double.compare(next[i] - prev[i],
                next[j] - prev[j]));
        for (int i = 0; i < n; i++) {
            pq.add(i);
        }
        KahamSummation sum = new KahamSummation();
        sum.add(n + 1);
        sum.subtract(Math.pow(Math.E, now.sum()));
        long end = System.currentTimeMillis() + 1500;
        int round = 0;
        while (round < 1000000) {
            round++;
            int peek = pq.pop();
            now.add(next[peek]);
            now.subtract(prev[peek]);
            prev[peek] = next[peek];
            time[peek]++;
            next[peek] = success(q[peek], time[peek] + 1);
            pq.add(peek);
            sum.add(1);
            double sub = Math.pow(Math.E, now.sum());
            sum.subtract(sub);
           // debug.debug("sub", sub);
        }

        out.println(sum.sum());
        debug.debug("round", round);
    }


}
