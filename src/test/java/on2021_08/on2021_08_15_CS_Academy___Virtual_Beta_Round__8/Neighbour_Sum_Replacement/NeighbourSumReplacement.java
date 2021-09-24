package on2021_08.on2021_08_15_CS_Academy___Virtual_Beta_Round__8.Neighbour_Sum_Replacement;



import template.io.FastInput;
import template.io.FastOutput;

public class NeighbourSumReplacement {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        out.println(n);
        for (int i = 0; i < n; i++) {
            int prev = i == 0 ? a[n - 1] : a[i - 1];
            int next = i == n - 1 ? a[0] : a[i + 1];
            out.append(prev + next).append(' ');
        }
    }
}
