package contest;

import template.io.FastInput;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DLetsPlayNim {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        if (n % 2 == 1) {
            out.println("Second");
            return;
        }
        Map<Integer, Integer> cntMap = new HashMap<>();
        for (int x : a) {
            cntMap.put(x, cntMap.getOrDefault(x, 0) + 1);
        }
        for (int v : cntMap.values()) {
            if (v % 2 == 1) {
                out.println("First");
                return;
            }
        }
        out.println("Second");
    }
}
