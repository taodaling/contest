package on2021_07.on2021_07_13_Luogu.P1886_______________;



import template.datastructure.RMQBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;

public class P1886 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        RMQBeta rmq = new RMQBeta(n);
        rmq.init(n, (i, j) -> Integer.compare(a[i], a[j]));
        for (int i = k - 1; i < n; i++) {
            out.append(a[rmq.query(i - k + 1, i)]).append(' ');
        }
        out.println();
        rmq.init(n, (i, j) -> -Integer.compare(a[i], a[j]));
        for (int i = k - 1; i < n; i++) {
            out.append(a[rmq.query(i - k + 1, i)]).append(' ');
        }
        out.println();
    }
}
