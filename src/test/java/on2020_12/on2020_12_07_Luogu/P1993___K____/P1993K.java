package on2020_12.on2020_12_07_Luogu.P1993___K____;



import template.graph.DifferSystem;
import template.io.FastInput;
import template.io.FastOutput;

public class P1993K {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DifferSystem sys = new DifferSystem(n);
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (t == 1) {
                int c = in.ri();
                sys.greaterThanOrEqualTo(a, b, c);
            } else if (t == 2) {
                int c = in.ri();
                sys.lessThanOrEqualTo(a, b, c);
            } else {
                sys.equalTo(a, b, 0);
            }
        }
        if(!sys.hasSolution()){
            out.println("No");
            return;
        }
        out.println("Yes");
    }
}
