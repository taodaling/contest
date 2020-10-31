package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Stick_Lengths;



import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerComparator;
import template.utils.CompareUtils;

import java.io.PrintWriter;

public class StickLengths {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] p = new int[n];
        in.populate(p);
        CompareUtils.theKthSmallestElement(p, IntegerComparator.NATURE_ORDER,
                0, n, DigitUtils.ceilAverage(n, 2));
        int mid = p[DigitUtils.ceilDiv(n, 2) - 1];
        long sum = 0;
        for(int x : p){
            sum += Math.abs(x - mid);
        }
        out.println(sum);
    }
}
