package on2021_07.on2021_07_09_Library_Checker.Static_RMQ0;



import template.datastructure.RMQBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class StaticRMQ {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        RMQBeta rmq = new RMQBeta(n, (i, j) -> Integer.compare(a[i], a[j]));
        for (int i = 0; i < q; i++) {
            int l = in.ri();
            int r = in.ri() - 1;
            int index = rmq.query(l, r);
            out.println(a[index]);
        }
    }
}
