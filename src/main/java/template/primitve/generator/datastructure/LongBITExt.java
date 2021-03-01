package template.primitve.generated.datastructure;

import java.util.Arrays;

public class LongBITExt {
    long[] delta;
    long[] idelta;
    int n;

    public LongBITExt(int n) {
        this.n = n + 1;
        delta = new long[n + 2];
        idelta = new long[n + 2];
    }

    public void clear(int n) {
        this.n = n + 1;
        Arrays.fill(delta, 1, n + 2, 0);
        Arrays.fill(idelta, 1, n + 2, 0);
    }

    public void clear() {
        clear(n);
    }

    public void clear(IntToLongFunction func, int m) {
        clear(m);
        long last = 0;
        for (int i = 1; i < n; i++) {
            long x = func.apply(i);
            long d = x - last;
            last = x;
            delta[i] += d;
            idelta[i] += d * i;
            int to = i + (i & -i);
            if (to <= n) {
                delta[to] += delta[i];
                idelta[to] += idelta[i];
            }
        }
    }

    public void update(int l, int r, long x) {
        long x1 = x;
        long x2 = l * x;
        for (int i = l; i <= n; i += (i & -i)) {
            delta[i] += x1;
            idelta[i] += x2;
        }
        x1 = -x;
        x2 = (r + 1) * -x;
        for (int i = r; i <= n; i += (i & -i)) {
            delta[i] += x1;
            idelta[i] += x2;
        }
    }

    public long query(int i) {
        long factor = i + 1;
        long ans = 0;
        for (; i > 0; i -= (i & -i)) {
            ans += factor * delta[i] - idelta[i];
        }
        return ans;
    }

    public long query(int l, int r) {
        return query(r) - query(l - 1);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("[");
        for (int i = 1; i < n; i++) {
            ans.append(query(i, i)).append(',');
        }
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append("]");
        return ans.toString();
    }
}
