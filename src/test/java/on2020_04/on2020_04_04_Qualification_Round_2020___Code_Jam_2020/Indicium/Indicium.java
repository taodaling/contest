package on2020_04.on2020_04_04_Qualification_Round_2020___Code_Jam_2020.Indicium;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

public class Indicium {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        int k = in.readInt();

        //case 1
        k -= n;

        int[][] mat = null;
        for (int d = 0; d < n && mat == null; d++) {

            mat = solve(n, k, d);
        }

        if (mat == null) {
            out.println("IMPOSSIBLE");
            return;
        }
        out.println("POSSIBLE");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(mat[i][j] + 1).append(' ');
            }
            out.pop(1);
            out.println();
        }
    }

    public int sumOf(int n) {
        return (1 + n) * n / 2;
    }

    public int sumOf(int l, int r) {
        return sumOf(r) - sumOf(l - 1);
    }

    public int[][] solve(int n, int k, int d) {
        if (GCDs.gcd(d, n) != 1) {
            return null;
        }

        int step = DigitUtils.mod(d - 1, n);
        int g = GCDs.gcd(n, step);
        int pick = n / g;
        int mul = n / pick;
        if (k % mul != 0) {
            return null;
        }
        k /= mul;
        if (k < sumOf(0, pick - 1) || k > sumOf(n - pick, n - 1)) {
            return null;
        }

        int[] vals = new int[pick];
        int sum = 0;
        for (int i = 0; i < pick; i++) {
            vals[i] = i;
            sum += vals[i];
        }

        int last = n;
        for (int i = pick - 1; i >= 0; i--) {
            int plus = Math.min(k - sum, last - 1 - vals[i]);
            vals[i] += plus;
            sum += plus;
            last = vals[i];
        }

        boolean[] used = new boolean[n];
        for (int x : vals) {
            used[x] = true;
        }
        IntegerList other = new IntegerList(n);
        IntegerList hit = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                other.add(i);
            } else {
                hit.add(i);
            }
        }
        int[] row = new int[n];
        for (int i = 0; i < n; i++) {
            if (i % g == 0) {
                row[i] = hit.pop();
            } else {
                row[i] = other.pop();
            }
        }

        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            mat[i] = row.clone();
            SequenceUtils.rotate(mat[i], 0, n - 1, (i * d) % n);
        }

        return mat;
    }

}
