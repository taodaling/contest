package on2021_04.on2021_04_09_Library_Checker.Associative_Array;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;

public class AssociativeArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        LongHashMap lhm = new LongHashMap(q, false);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                lhm.put(in.readLong(), in.readLong());
            } else {
                out.println(lhm.get(in.readLong()));
            }
        }
    }
}
