package on2021_07.on2021_07_13_Luogu.P3865_____ST__;



import template.datastructure.RMQBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class P3865ST {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        RMQBeta rmq = new RMQBeta(n);
        rmq.init(n, (x, y) -> -Integer.compare(a[x], a[y]));
        for(int i = 0; i < m; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            out.println(a[rmq.query(l, r)]);
        }
    }
}
