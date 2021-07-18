package on2021_07.on2021_07_18_Library_Checker.Associative_Array;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;

public class AssociativeArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        LongHashMap map = new LongHashMap(q, false);
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 0) {
                map.put(in.rl(), in.rl());
            } else {
                long ans = map.getOrDefault(in.rl(), 0L);
                out.println(ans);
            }
        }
    }
}
