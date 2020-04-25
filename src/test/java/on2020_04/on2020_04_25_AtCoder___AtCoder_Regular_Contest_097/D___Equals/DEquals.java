package on2020_04.on2020_04_25_AtCoder___AtCoder_Regular_Contest_097.D___Equals;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class DEquals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }
        DSU dsu = new DSU(n + n);
        for (int i = 0; i < n; i++) {
            dsu.merge(idOfIndex(i), idOfVal(p[i]));
        }

        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            dsu.merge(idOfIndex(a), idOfIndex(b));
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(idOfVal(p[i])) == dsu.find(idOfIndex(p[i]))) {
                ans++;
            }
        }

        out.println(ans);
    }

    int n;

    public int idOfIndex(int i) {
        return i;
    }

    public int idOfVal(int i) {
        return n + i;
    }
}
