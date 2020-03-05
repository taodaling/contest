package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;

public class EPinkFloyd {
    LongHashMap map = new LongHashMap(300000, false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        for (int i = 0; i < n; i++) {
            int u = in.readInt();
            int v = in.readInt();
            map.put(edgeId(u, v), u);
        }

        int root = 1;
        for (int i = 2; i <= n; i++) {
            int ans = ask(root, i);
            root = ans;
        }

        answer(root);
    }

    FastOutput out;
    FastInput in;

    public int ask(int u, int v) {
        long key = edgeId(u, v);
        if (!map.containKey(key)) {
            out.append("? ").append(u).append(' ').append(v).println().flush();
            int x = in.readInt();
            int src = x == 1 ? u : v;
            map.put(key, src);
        }
        return (int) map.get(key);
    }

    public void answer(int x) {
        out.append("! ").append(x).println().flush();
    }

    public long edgeId(int u, int v) {
        if (u > v) {
            int tmp = u;
            u = v;
            v = tmp;
        }
        return DigitUtils.asLong(u, v);
    }
}
