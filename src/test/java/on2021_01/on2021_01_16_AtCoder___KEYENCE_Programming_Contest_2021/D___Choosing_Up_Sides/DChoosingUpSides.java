package on2021_01.on2021_01_16_AtCoder___KEYENCE_Programming_Contest_2021.D___Choosing_Up_Sides;



import template.io.FastInput;
import template.io.FastOutput;

public class DChoosingUpSides {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int N = in.ri();
        int n = 1 << (N - 1);
        out.println(2 * n - 1);
        int[][] ans = commonIntersect(2 * n - 1);
        for (int i = 0; i < 2 * n - 1; i++) {
            for (int j = 0; j < ans.length; j++) {
                char c = (char) ('A' + ans[j][i]);
                out.append(c);
            }
            out.println();
        }
    }

    int[][][] mem = new int[256][][];

    public int[][] commonIntersect(int n) {
        assert Integer.lowestOneBit(n + 1) == n + 1;
        if (mem[n] == null) {
            if (n == 0) {
                return mem[n] = new int[1][n];
            }
            int r = (n + 1);
            int[][] mat = new int[r][];
            mat[0] = new int[n];
            int wpos = 1;
            for (int k = (n + 1) / 2; k >= 1; k /= 2) {
                int step = k * 2;
                int cnt = n / step;
                int[][] bits = commonIntersect(cnt);
                for (int[] bit : bits) {
                    int[] init = new int[n];
                    for (int j = 0; j < n; j++) {
                        int b = j / step >= bit.length ? 0 : bit[j / step];
                        init[j] = (j / k % 2) ^ 1 ^ b;
                    }
                    mat[wpos++] = init;
                }
            }
            mem[n] = mat;
            assert mat.length == (n + 1);
        }
        return mem[n];
    }
}
