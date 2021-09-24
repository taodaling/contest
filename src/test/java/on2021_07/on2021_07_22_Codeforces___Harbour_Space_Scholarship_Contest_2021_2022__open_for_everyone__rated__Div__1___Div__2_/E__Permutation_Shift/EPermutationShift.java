package on2021_07.on2021_07_22_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.E__Permutation_Shift;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Permutation;
import template.primitve.generated.datastructure.IntegerArrayList;

public class EPermutationShift {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int atleast = n - 2 * m;
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
        }
        int[] occur = new int[n];
        for (int i = 0; i < n; i++) {
            //a[i] + r = i
            //r = i - a[i];
            int indicate = i - p[i];
            indicate = DigitUtils.mod(indicate, n);
            occur[indicate]++;
        }
        IntegerArrayList ans = new IntegerArrayList();
        int[] clone = new int[n];
        for (int i = 0; i < n; i++) {
            if (occur[i] < atleast) {
                continue;
            }
            System.arraycopy(p, 0, clone, 0, n);
            int req = test(clone, i);
            if (req <= m) {
                ans.add(i);
            }
        }
        out.append(ans.size()).append(' ');
        for (int x : ans.toArray()) {
            out.append(x).append(' ');
        }
        out.println();
    }

    public int test(int[] p, int r) {
        int n = p.length;
        for (int i = 0; i < n; i++) {
            p[i] += r;
            p[i] = DigitUtils.mod(p[i], n);
        }
        Permutation perm = new Permutation(p);
        int c = perm.countCircles();
        return n - c;
    }
}
