package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearFeedbackShiftRegister;

public class FindLinearRecurrence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int mod = 998244353;
        ModLinearFeedbackShiftRegister solver = new ModLinearFeedbackShiftRegister(mod, n);
        for (int x : a) {
            solver.add(x);
        }
        out.println(solver.length());
        for (int i = 1; i <= solver.length(); i++) {
            out.append(solver.codeAt(i)).append(' ');
        }
    }
}
