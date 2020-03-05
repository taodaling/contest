package on2020_03.on2020_03_05_Codeforces_Round__549__Div__1_.B__Lynyrd_Skynyrd;



import template.binary.CachedLog2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.primitve.generated.datastructure.IntegerSparseTable;
import template.utils.SequenceUtils;

public class BLynyrdSkynyrd {
    int[][] jump;
    int m;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        m = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        int[] invA = new int[n + 1];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            invA[a[i]] = i;
        }
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = invA[in.readInt()];
        }

        jump = new int[m][20];
        SequenceUtils.deepFill(jump, m);
        int[] registry = new int[n];
        SequenceUtils.deepFill(registry, m);
        for (int i = m - 1; i >= 0; i--) {
            int next = (b[i] + 1) % n;
            jump[i][0] = registry[next];
            registry[b[i]] = i;
            for (int j = 0; jump[i][j] != m; j++) {
                jump[i][j + 1] = jump[jump[i][j]][j];
            }
        }

        int[] right = new int[m];
        for (int i = 0; i < m; i++) {
            right[i] = jump(i, n - 1);
        }

        IntegerSparseTable st = new IntegerSparseTable(right, m, (x, y) -> Math.min(x, y));
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int min = st.query(l, r);
            out.append(min <= r ? '1' : '0');
        }
    }

    public int jump(int i, int k) {
        if (i == m) {
            return i;
        }
        if (k == 0) {
            return i;
        }
        int log = CachedLog2.floorLog(k);
        return jump(jump[i][log], k - (1 << log));
    }
}
