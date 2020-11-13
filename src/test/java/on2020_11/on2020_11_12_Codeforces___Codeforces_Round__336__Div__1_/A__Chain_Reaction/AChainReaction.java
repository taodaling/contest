package on2020_11.on2020_11_12_Codeforces___Codeforces_Round__336__Div__1_.A__Chain_Reaction;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class AChainReaction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Bacon[] bacons = new Bacon[n];
        for (int i = 0; i < n; i++) {
            bacons[i] = new Bacon(in.readInt(), in.readInt());
        }
        Arrays.sort(bacons, (a, b) -> Integer.compare(a.a, b.a));
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            map.put(bacons[i].a, i);
        }
        int[] retain = new int[n];
        for (int i = 0; i < n; i++) {
            int left = map.ceilingEntry(bacons[i].a - bacons[i].b).getValue();
            retain[i] = left > 0 ? retain[left - 1] : 0;
            retain[i]++;
        }
        int ans = Arrays.stream(retain).max().orElse(-1);
        out.println(n - ans);
    }
}


class Bacon {
    int a;
    int b;

    public Bacon(int a, int b) {
        this.a = a;
        this.b = b;
    }
}