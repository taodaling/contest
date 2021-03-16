package template.primitve.generated.datastructure;

import java.util.Arrays;

public class IntegerBITExt {
    int[] delta;
    int[] idelta;
    int n;

    public IntegerBITExt(int n) {
        this.n = n;
        delta = new int[n + 1];
        idelta = new int[n + 1];
    }

    public void clear(int n) {
        this.n = n;
        Arrays.fill(delta, 1, n + 1, 0);
        Arrays.fill(idelta, 1, n + 1, 0);
    }

    public void clear() {
        clear(n);
    }

    public void clear(IntToIntegerFunction func, int n) {
        this.n = n;
        for (int i = 1; i <= n; i++) {
            delta[i] = func.apply(i);
        }
        for (int i = n; i > 0; i--) {
            delta[i] = delta[i] - delta[i - 1];
            idelta[i] = delta[i] * i;
        }
        for (int i = 1; i <= n; i++) {
            int to = i + (i & -i);
            if (to <= n) {
                delta[to] += delta[i];
                idelta[to] += idelta[i];
            }
        }
    }

    private void update(int i, int x) {
        int x1 = x;
        int x2 = x * i;
        for (; i <= n; i += i & -i) {
            delta[i] += x1;
            idelta[i] += x2;
        }
    }

    public void update(int l, int r, int x) {
        update(l, x);
        update(r + 1, -x);
    }

    public int query(int x) {
        int ans1 = 0;
        int ans2 = 0;
        for (int i = x; i > 0; i -= (i & -i)) {
            ans1 += delta[i];
            ans2 += idelta[i];
        }
        return (x + 1) * ans1 - ans2;
    }

    public int query(int l, int r) {
        return query(r) - query(l - 1);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("[");
        for (int i = 1; i <= n; i++) {
            ans.append(query(i, i)).append(',');
        }
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append("]");
        return ans.toString();
    }
}
