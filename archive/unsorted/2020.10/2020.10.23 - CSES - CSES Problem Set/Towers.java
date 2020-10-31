package contest;

import template.datastructure.MultiSet;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Towers {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        MultiSet<Integer> set = new MultiSet<>();
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            Integer ceil = set.ceil(x + 1);
            if (ceil == null) {
                set.add(x);
            } else {
                set.remove(ceil);
                set.add(x);
            }
        }
        out.println(set.size());
    }
}
