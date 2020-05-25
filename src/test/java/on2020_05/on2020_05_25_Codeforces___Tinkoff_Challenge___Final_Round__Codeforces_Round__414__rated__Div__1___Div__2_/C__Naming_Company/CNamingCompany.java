package on2020_05.on2020_05_25_Codeforces___Tinkoff_Challenge___Final_Round__Codeforces_Round__414__rated__Div__1___Div__2_.C__Naming_Company;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

public class CNamingCompany {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 3e5];
        char[] t = new char[(int) 3e5];
        int n = in.readString(s, 0);
        in.readString(t, 0);

        Counter a = new Counter(s, n);
        Counter b = new Counter(t, n);

        int aPut = (n + 1) / 2;
        int bPut = n - aPut;
        for(int i = 0; i < n - aPut; i++){
            a.max();
            a.popMax();
        }
        for(int i = 0; i < n - bPut; i++){
            b.min();
            b.popMin();
        }

        int[] ans = new int[n];
        dfs(a, b, ans, 0, n - 1);
        for (int i = 0; i < n; i++) {
            ans[i] = Math.abs(ans[i]);
        }
        for (int i = 0; i < n; i++) {
            out.append((char) ans[i]);
        }
    }

    public void dfs(ICounter a, ICounter b, int[] ans, int l, int r) {
        if (l > r) {
            return;
        }
        if (a.min() < b.max()) {
            ans[l++] = a.min();
            a.popMin();
        } else {
            ans[r--] = a.max();
            a.popMax();
        }
        dfs(b.inverse(), a.inverse(), ans, l, r);
    }
}

interface ICounter {
    int max();

    void popMax();

    int min();

    void popMin();

    ICounter inverse();
}

class InvCounter implements ICounter {
    ICounter inner;

    public InvCounter(ICounter inner) {
        this.inner = inner;
    }

    @Override
    public int max() {
        return -inner.min();
    }

    @Override
    public void popMax() {
        inner.popMin();
    }

    @Override
    public int min() {
        return -inner.max();
    }

    @Override
    public void popMin() {
        inner.popMax();
    }

    @Override
    public ICounter inverse() {
        return inner;
    }
}

class Counter implements ICounter {
    int[] cnts = new int[128];
    int l = 0;
    int r = cnts.length - 1;
    InvCounter inv = new InvCounter(this);

    public Counter(char[] s, int n) {
        for (int i = 0; i < n; i++) {
            cnts[s[i]]++;
        }
    }

    private void handleLeft() {
        while (l <= r && cnts[l] == 0) {
            l++;
        }
    }

    private void handleRight() {
        while (l <= r && cnts[r] == 0) {
            r--;
        }
    }

    public int max() {
        handleRight();
        return r;
    }

    public int min() {
        handleLeft();
        return l;
    }

    public void popMin() {
        cnts[l]--;
    }


    public void popMax() {
        cnts[r]--;
    }

    @Override
    public ICounter inverse() {
        return inv;
    }
}
