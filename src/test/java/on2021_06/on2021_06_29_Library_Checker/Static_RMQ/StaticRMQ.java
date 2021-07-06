package on2021_06.on2021_06_29_Library_Checker.Static_RMQ;



import template.datastructure.RMQBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class StaticRMQ {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        RMQBeta rmq = new RMQBeta(n, (i, j) -> Integer.compare(a[i], a[j]));
        for(int i = 0; i < q; i++){
            int l = in.ri();
            int r = in.ri() - 1;
            int index = rmq.query(l, r);
            out.println(a[index]);
        }
    }
}
