package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.HashData;
import template.rand.ModifiableHash;

import java.io.PrintWriter;

public class GridPaths {
    int size = 7;
    boolean[][] mat = new boolean[size][size];
    char[] seq = new char[size * size - 1];
    int[][] dirs = new int[][]{
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };


    int[][] time = new int[7][7];
    int now;

    LongHashMap map = new LongHashMap((int) 1e6, true);
    HashData h1 = new HashData(100, (int) 1e9 + 7, 31);
    HashData h2 = new HashData(100, (int) 1e9 + 7, 61);
    ModifiableHash hash = new ModifiableHash(h1, h2, 100);


    public boolean prune(int i, int j) {
        if (i == 0 && mat[i + 1][j] && j > 0 && j + 1 < size &&
                !mat[i][j - 1] && !mat[i][j + 1]) {
            return true;
        }
        if (i == size - 1 && mat[i - 1][j] && j > 0 && j + 1 < size &&
                !mat[i][j - 1] && !mat[i][j + 1]) {
            return true;
        }
        if (j == 0 && mat[i][j + 1] && i > 0 && i + 1 < size &&
                !mat[i + 1][j] && !mat[i - 1][j]) {
            return true;
        }
        if (j == size - 1 && mat[i][j - 1] && i > 0 && i + 1 < size &&
                !mat[i + 1][j] && !mat[i - 1][j]) {
            return true;
        }
        return false;
    }

    public void set(int i, int j, int step) {
        mat[i][j] = true;
//        hash.modify(i * size + j, 1);
//        hash.modify(50, i);
//        hash.modify(51, j);
//        hash.modify(52, step);
    }


    public void clear(int i, int j, int step) {
        mat[i][j] = false;
        //hash.modify(i * size + j, 0);
    }

    public int dfs0(int i, int j, int step) {
        if (i == size - 1 && j == 0) {
            return step == seq.length ? 1 : 0;
        }
        if (step >= seq.length) {
            return 0;
        }
        if (prune(i, j)) {
            return 0;
        }

        int ans = 0;
        if (seq[step] == '?') {
            for (int[] d : dirs) {
                ans += dfs(d[0] + i, d[1] + j, step + 1);
            }
        } else {
            int[] d;
            if (seq[step] == 'L') {
                d = dirs[3];
            } else if (seq[step] == 'R') {
                d = dirs[2];
            } else if (seq[step] == 'U') {
                d = dirs[1];
            } else {
                d = dirs[0];
            }
            ans += dfs(d[0] + i, j + d[1], step + 1);
        }
        return ans;
    }

    public int dfs(int i, int j, int step) {
        if (i < 0 || i >= size || j < 0 || j >= size || mat[i][j]) {
            return 0;
        }
        set(i, j, step);
//        long h = hash.hash();
//        long ans = map.getOrDefault(h, -1);
//
//        if (ans == -1) {
        int ans = dfs0(i, j, step);
//            map.put(h, ans);
//        }
//
        clear(i, j, step);
//        return (int) ans;
        return ans;
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        in.readString(seq, 0);
        int ans = dfs(0, 0, 0);
        out.println(ans);
    }
}
