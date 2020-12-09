package on2020_12.on2020_12_08_POJ___POJ_Monthly__2006_12_31.Candies;



import template.graph.DifferSystem;
import template.io.FastInput;
import template.io.FastOutput;

public class Candies {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DifferSystem ds = new DifferSystem(n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri();
            ds.lessThanOrEqualTo(b, a, c);
        }
        ds.runSpfaSince(0);
        out.println(ds.possibleSolutionOf(n - 1));
    }
}
