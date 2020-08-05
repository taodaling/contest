package on2020_08.on2020_08_04_Codeforces___Codeforces_Round__373__Div__1_.C__Sasha_and_Array;



import com.sun.org.apache.xpath.internal.operations.Mod;
import template.datastructure.GenericSegment;
import template.io.FastInput;
import template.io.FastOutput;

public class CSashaAndArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        GenericSegment<Summary, Modify> seg = new GenericSegment<>(1, n, Modify::new,
                Summary::new, i -> new Summary());
        Modify mod = new Modify();
        int[][] fib = new int[][]{
                {0, 1},
                {1, 1}
        };
        for (int i = 1; i <= n; i++) {
            pow(fib, in.readInt(), mod.mat);
            seg.update(i, i, 1, n, mod);
        }
        Summary summary = new Summary();
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            int l = in.readInt();
            int r = in.readInt();
            if (t == 1) {
                pow(fib, in.readInt(), mod.mat);
                seg.update(l, r, 1, n, mod);
            } else {
                summary.vec[0] = summary.vec[1] = 0;
                seg.query(l, r, 1, n, summary);
                out.println(summary.vec[0]);
            }
        }
    }

    public static void asStandard(int[][] mat) {
        mat[0][0] = mat[1][1] = 1;
        mat[0][1] = mat[1][0] = 0;
    }

    public static void pow(int[][] x, int n, int[][] ans) {
        if (n == 0) {
            asStandard(ans);
            return;
        }
        pow(x, n / 2, ans);
        mul(ans, ans, ans);
        if (n % 2 == 1) {
            mul(ans, x, ans);
        }
    }

    static int mod = (int) (1e9 + 7);

    static int[][] mul(int[][] a, int[][] b, int[][] ans) {
        int a00 = (int) (((long) a[0][0] * b[0][0] + (long) a[0][1] * b[1][0]) % mod);
        int a01 = (int) (((long) a[0][0] * b[0][1] + (long) a[0][1] * b[1][1]) % mod);
        int a10 = (int) (((long) a[1][0] * b[0][0] + (long) a[1][1] * b[1][0]) % mod);
        int a11 = (int) (((long) a[1][0] * b[0][1] + (long) a[1][1] * b[1][1]) % mod);
        ans[0][0] = a00;
        ans[0][1] = a01;
        ans[1][0] = a10;
        ans[1][1] = a11;
        return ans;
    }
}

class Modify implements GenericSegment.Modify<Summary, Modify> {
    static int mod = (int) 1e9 + 7;
    int[][] mat;

    public Modify() {
        mat = new int[2][2];
        clear();
    }


    @Override
    public void modify(Summary summary) {
        long a = (long) summary.vec[0] * mat[0][0] + (long) summary.vec[1] * mat[0][1];
        long b = (long) summary.vec[0] * mat[1][0] + (long) summary.vec[1] * mat[1][1];
        summary.vec[0] = (int) (a % mod);
        summary.vec[1] = (int) (b % mod);
    }

    @Override
    public void merge(Modify modify) {
        CSashaAndArray.mul(mat, modify.mat, mat);
    }

    @Override
    public void clear() {
        mat[0][0] = mat[1][1] = 1;
        mat[1][0] = mat[0][1] = 0;
    }

    @Override
    public Modify clone() {
        return new Modify();
    }


}

class Summary implements GenericSegment.Summary<Summary> {
    static int mod = (int) 1e9 + 7;
    int[] vec = new int[]{0, 1};

    @Override
    public void merge(Summary a) {
        for (int i = 0; i < 2; i++) {
            vec[i] = (vec[i] + a.vec[i]) % mod;
        }
    }

    @Override
    public void merge(Summary a, Summary b) {
        for (int i = 0; i < 2; i++) {
            vec[i] = (b.vec[i] + a.vec[i]) % mod;
        }
    }

    @Override
    public Summary clone() {
        return new Summary();
    }
}