package on2020_11.on2020_11_05_CSES___CSES_Problem_Set.Range_Sum_Queries_II;



import template.datastructure.RMQ;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongBIT;

public class RangeSumQueriesII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        LongBIT bit = new LongBIT(n);
        for (int i = 0; i < n; i++) {
            bit.update(i + 1, a[i]);
        }
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if(t == 1){
                int k = in.readInt();
                int x = in.readInt();
                bit.set(k, x);
            }else {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                out.println(bit.query(l + 1, r + 1));
            }
        }
    }
}
