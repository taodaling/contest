package on2021_05.on2021_05_07_Codeforces___Codeforces_Round__189__Div__1_.B__Psychos_in_a_Line;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.TreeSet;

public class BPsychosInALine {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] p = in.ri(n);
        TreeSet<Integer> s = new TreeSet<>();
        TreeSet<Integer> prev = new TreeSet<>();
        TreeSet<Integer> next = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            s.add(i);
            if (i + 1 < n && p[i] > p[i + 1]) {
                prev.add(i + 1);
            }
        }
        int ans = 0;
        while (!prev.isEmpty()) {
            ans++;
            next.clear();
            s.removeAll(prev);
            for (Integer x : prev) {
                Integer r = s.ceiling(x);
                Integer l = s.floor(x);
                if (l != null && r != null && p[l] > p[r]) {
                    next.add(r);
                }
            }
            TreeSet<Integer> tmp = next;
            next = prev;
            prev = tmp;
        }

        out.println(ans);
    }
}
