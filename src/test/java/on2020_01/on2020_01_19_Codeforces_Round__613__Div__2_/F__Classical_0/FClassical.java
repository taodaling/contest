package on2020_01.on2020_01_19_Codeforces_Round__613__Div__2_.F__Classical_0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LCMs;
import template.problem.GcdLcmProblem;

public class FClassical {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        GcdLcmProblem problem = new GcdLcmProblem(100000, a);
        int[] pair = problem.findAnyPairIndexesWhileLCMMaximized();
        long lcm = LCMs.lcm(a[pair[0]], a[pair[1]]);
        out.println(lcm);
    }
}
