package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerHashMap;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

public class SumOfTwoValues {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        HashMap<Integer, Integer> set = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (set.containsKey(x - a[i])) {
                out.println(set.get(x - a[i]) + 1);
                out.println(i + 1);
                return;
            }
            set.put(a[i], i);
        }
        out.println("IMPOSSIBLE");
    }
}
