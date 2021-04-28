package on2021_04.on2021_04_26_Codeforces___Codeforces_Round__198__Div__1_.D__Iahub_and_Xors;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.Int2ToLongFunction;

public class DIahubAndXors {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        LongBIT2DExt bit = new LongBIT2DExt(n, n);
        for(int i = 0; i < m; i++){
            int t = in.ri();
            int x0 = in.ri();
            int y0 = in.ri();
            int x1 = in.ri();
            int y1 = in.ri();
            if(t == 2){
                long v = in.rl();
                bit.update(x0, y0, x1, y1, v);
            }else{
                long ans = bit.query(x0, y0, x1, y1);
                out.println(ans);
            }
        }
    }
}

class LongBIT2DExt {
    long[][] delta;
    long[][] idelta;
    long[][] jdelta;
    long[][] ijdelta;
    int n;
    int m;

    public LongBIT2DExt(int n, int m) {
        this.n = n;
        this.m = m;
        delta = new long[n + 1][m + 1];
        idelta = new long[n + 1][m + 1];
        jdelta = new long[n + 1][m + 1];
        ijdelta = new long[n + 1][m + 1];
    }

    long mul(long a, long time) {
        return (time & 1) == 1 ? a : 0;
    }

    private void update(int x, int y, long mod) {
        long x1 = mod;
        long x2 = mul(mod , x);
        long x3 = mul(mod , y);
        long x4 = mul(mul(mod, x), y);
        for (int i = x; i <= n; i += i & -i) {
            for (int j = y; j <= m; j += j & -j) {
                delta[i][j] ^= x1;
                idelta[i][j] ^= x2;
                jdelta[i][j] ^= x3;
                ijdelta[i][j] ^= x4;
            }
        }
    }

    public void update(int ltx, int lty, int rbx, int rby, long mod) {
        update(ltx, lty, mod);
        update(rbx + 1, lty, mod);
        update(ltx, rby + 1, mod);
        update(rbx + 1, rby + 1, mod);
    }

    public long query(int x, int y) {
        long ans1 = 0;
        long ans2 = 0;
        long ans3 = 0;
        long ans4 = 0;
        for (int i = x; i > 0; i -= i & -i) {
            for (int j = y; j > 0; j -= j & -j) {
                ans1 ^= delta[i][j];
                ans2 ^= idelta[i][j];
                ans3 ^= jdelta[i][j];
                ans4 ^= ijdelta[i][j];
            }
        }
        return mul(mul(ans1, (x + 1)), (y + 1)) ^ mul(ans2, (y + 1)) ^ mul(ans3, (x + 1)) ^ ans4;
    }

    public long query(int ltx, int lty, int rbx, int rby) {
        long ans = query(rbx, rby);
        if (ltx > 1) {
            ans ^= query(ltx - 1, rby);
        }
        if (lty > 1) {
            ans ^= query(rbx, lty - 1);
        }
        if (ltx > 1 && lty > 1) {
            ans ^= query(ltx - 1, lty - 1);
        }
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                builder.append(query(i, j) + query(i - 1, j - 1) - query(i - 1, j) - query(i, j - 1)).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
