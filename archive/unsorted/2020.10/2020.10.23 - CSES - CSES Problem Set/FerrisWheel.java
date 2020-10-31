package contest;

import template.datastructure.MultiSet;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeSet;

public class FerrisWheel {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int t = in.readInt();
        int[] x = new int[n];
        in.populate(x);
        MultiSet<Integer> set = new MultiSet<>();
        for (int a : x) {
            set.add(a);
        }
        int ans = 0;
        while (set.size() > 0) {
            int head = set.pollFirst();
            Integer partner = set.floor(t - head);
            if (partner != null) {
                set.remove(partner);
            }
            ans++;
        }
        out.println(ans);
    }
}
