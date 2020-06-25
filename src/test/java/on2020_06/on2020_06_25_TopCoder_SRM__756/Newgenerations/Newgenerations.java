package on2020_06.on2020_06_25_TopCoder_SRM__756.Newgenerations;




import template.math.CachedPow;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;

public class Newgenerations {
    char[][] mat;
    int[][] occur;
    int remain;
    int n;
    int m;
    int[] x;
    int[] y;
    boolean[] possible;

    public boolean match(int i, int j) {
        return !(i < 0 || j < 0 || i >= n || j >= m || mat[i][j] != '*');
    }

    public void set(int i, int j) {
        if (!match(i, j)) {
            return;
        }
        occur[i][j]++;
        if (occur[i][j] == 1) {
            remain--;
        }
    }

    public void clear(int i, int j) {
        if (!match(i, j)) {
            return;
        }
        occur[i][j]--;
        if (occur[i][j] == 0) {
            remain++;
        }
    }

    public boolean possible(int i, int j) {
        if (i == 0 || j == 0 || i == n - 1 || j == m - 1) {
            return false;
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if(Math.abs(dx) + Math.abs(dy) != 1){
                    continue;
                }
                if (mat[i + dx][j + dy] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public int ie(int k, int pick) {
        if (k < 0) {
            int ans = pow.pow(remain);
            if (pick % 2 == 1) {
                ans = mod.valueOf(-ans);
            }
            return ans;
        }

        int ans = 0;
        ans = mod.plus(ans, ie(k - 1, pick));

        if (possible[k]) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (Math.abs(i) + Math.abs(j) != 1) {
                        continue;
                    }
                    set(x[k] + i, y[k] + j);
                }
            }
            ans = mod.plus(ans, ie(k - 1, pick + 1));
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (Math.abs(i) + Math.abs(j) != 1) {
                        continue;
                    }
                    clear(x[k] + i, y[k] + j);
                }
            }
        }

        return ans;
    }

    Modular mod = new Modular(1e9 + 7);
    CachedPow pow = new CachedPow(2, mod);

    public int count(String[] field) {
        n = field.length;
        m = field[0].length();
        mat = new char[n][];
        occur = new int[n][m];


        IntegerList x = new IntegerList();
        IntegerList y = new IntegerList();
        for (int i = 0; i < n; i++) {
            mat[i] = field[i].toCharArray();
            for (int j = 0; j < m; j++) {
                char c = mat[i][j];
                if (c == '*') {
                    remain++;
                }
                if (c == 'x') {
                    x.add(i);
                    y.add(j);
                }
            }
        }

        this.x = x.toArray();
        this.y = y.toArray();
        possible = new boolean[this.x.length];
        for (int i = 0; i < this.x.length; i++) {
            possible[i] = possible(this.x[i], this.y[i]);
        }
        int ans = ie(x.size() - 1, 0);

        return ans;
    }


}
