package on2020_10.on2020_10_24_Library_Checker.Associative_Array;



import template.io.FastInput;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;

public class AssociativeArray {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int q = in.readInt();
        LongHashMap map = new LongHashMap(q, false);
        for (int i = 0; i < q; i++) {
            if (in.readInt() == 0) {
                map.put(in.readLong(), in.readLong());
            } else {
                out.println(map.get(in.readLong()));
            }
        }
    }
}
