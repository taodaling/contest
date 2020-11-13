package on2020_11.on2020_11_13_CSES___CSES_Problem_Set.Bit_Problem;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;

public class BitProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] x = new int[n];
        in.populate(x);
        int[] cnts = new int[1 << 20];
        for (int t : x) {
            cnts[t]++;
        }
        int[] subset = cnts.clone();
        int[] superset = cnts.clone();
        FastWalshHadamardTransform.orFWT(subset, 0, subset.length - 1);
        FastWalshHadamardTransform.andFWT(superset, 0, superset.length - 1);
        int mask = (1 << 20) - 1;
        for (int t : x) {
            out.append(subset[t]).append(' ').append(superset[t]).append(' ')
                    .append(n - subset[mask - t]).println();
        }
    }
}
