package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Modular;
import template.primitve.generated.IntegerList;

public class HSelfExploration {
    Modular mod = new Modular(1e9 + 7);
    Composite comp = new Composite(100000, mod);
    IntegerList blockOfA;
    IntegerList blockOfB;
    IntegerList actualBlock;

    /**
     * a1 + a2 + ... + an = m
     * @param n
     * @param m
     * @return
     */
    public int comp(int n, int m){
        if(n < 0 || m < 0){
            return 0;
        }
        if(n == 0 && m == 0){
            return 1;
        }
        return comp.composite(n + m - 1, m);
    }

    public int way(int one, int zero, int c1, int c0) {

        int p1 = comp(one, c1);
        int p2 = comp(zero, c0);
        return mod.mul(p1, p2);
    }

    public int dp(boolean ceil, boolean floor, int c11, int c00, int i) {
        if (c11 < 0 || c00 < 0) {
            return 0;
        }
        if (i == actualBlock.size()) {
            return c11 == 0 && c00 == 0 ? 1 : 0;
        }
        int oneBlock = (actualBlock.size() + 1) / 2 - (i + 1) / 2;
        int zeroBlock = actualBlock.size() - i - oneBlock;
        if (!ceil && !floor) {
            return way(oneBlock, zeroBlock, c11, c00);
        }
        if (!ceil) {
            int limit = blockOfA.get(i) - 1;
            if (i % 2 == 1) {
                int ans = 0;
                for (int j = 0; j <= limit; j++) {
                    ans = mod.plus(dp(ceil, j == limit, c11, c00 - j, i + 1), ans);
                }
                return ans;
            } else {
                int addition = way(oneBlock, zeroBlock, c11 - limit - 1, c00);
                int ans = dp(ceil, floor, c11 - limit, c00, i + 1);
                return mod.plus(ans, addition);
            }
        }
        if (!floor) {
            int limit = blockOfB.get(i) - 1;
            if (i % 2 == 0) {
                int ans = 0;
                for (int j = 0; j <= limit; j++) {
                    ans = mod.plus(dp(j == limit, floor, c11 - j, c00, i + 1), ans);
                }
                return ans;
            } else {
                int addition = way(oneBlock, zeroBlock, c11, c00 - limit - 1);
                int ans = dp(ceil, floor, c11, c00 - limit, i + 1);
                return mod.plus(ans, addition);
            }
        }
        int ans = 0;
        if (i % 2 == 0) {
            int l = blockOfA.get(i) - 1;
            int r = blockOfB.get(i) - 1;
            for (int j = l; j <= r; j++) {
                ans = mod.plus(dp(j == r, j == l, c11 - j, c00, i + 1), ans);
            }
        } else {
            int l = blockOfB.get(i) - 1;
            int r = blockOfA.get(i) - 1;
            for (int j = l; j <= r; j++) {
                ans = mod.plus(dp(j == l, j == r, c11, c00 - j, i + 1), ans);
            }
        }
        return ans;
    }

    private IntegerList asBlock(int[] x, int n) {
        int cnt = 1;
        int last = x[0];
        IntegerList blocks = new IntegerList(x.length);
        for (int i = 1; i < n; i++) {
            if (x[i] != last) {
                blocks.add(cnt);
                cnt = 0;
                last = x[i];
            }
            cnt++;
        }
        blocks.add(cnt);
        return blocks;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] a = new int[100000];
        int[] b = new int[100000];
        int aLen = in.readString(a, 0);
        int bLen = in.readString(b, 0);
        for (int i = 0; i < aLen; i++) {
            a[i] -= '0';
        }
        for (int i = 0; i < bLen; i++) {
            b[i] -= '0';
        }

        int[][] c = new int[2][2];
        int actualLength = 1;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                c[i][j] = in.readInt();
                actualLength += c[i][j];
            }
        }

        if (c[1][0] == 0 && (c[0][0] > 0 || c[0][1] > 0)) {
            out.println(0);
            return;
        }
        if (c[1][0] - c[0][1] > 1 || c[1][0] < c[0][1]) {
            out.println(0);
            return;
        }



        actualBlock = new IntegerList(c[1][0] + c[0][1]);
        for (int i = 0; i < 1 + c[1][0] + c[0][1]; i++) {
            actualBlock.add(1);
        }

        if (aLen > actualLength || bLen < actualLength) {
            out.println(0);
            return;
        }
        if (aLen < actualLength) {
            a[0] = 1;
            for (int i = 1; i < actualLength; i++) {
                a[i] = 0;
            }
        }
        if (bLen > actualLength) {
            for (int i = 0; i < actualLength; i++) {
                b[i] = 1;
            }
        }

        blockOfA = asBlock(a, actualLength);
        blockOfB = asBlock(b, actualLength);

        int ans = dp(true, true, c[1][1], c[0][0], 0);
        out.println(ans);
    }
}
