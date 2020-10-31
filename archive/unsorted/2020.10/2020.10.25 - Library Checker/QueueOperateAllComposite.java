package contest;

import template.io.FastInput;
import template.math.ModLinearFunction;
import template.math.Power;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class QueueOperateAllComposite {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int q = in.readInt();
        int mod = 998244353;
        Power pow = new Power(mod);
        Deque<ModLinearFunction> dq = new ArrayDeque<>(q);
        ModLinearFunction func = ModLinearFunction.IDENTITY;
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                int a = in.readInt();
                int b = in.readInt();
                ModLinearFunction fi = new ModLinearFunction(a, b);
                dq.addLast(fi);
                func = ModLinearFunction.merge(fi, func, mod);
            } else if (t == 1) {
                ModLinearFunction fi = dq.removeFirst();
                func = ModLinearFunction.merge(func, ModLinearFunction.inverse(fi, mod, pow), mod);
            } else {
                int x = in.readInt();
                int ans = func.apply(x, mod);
                out.println(ans);
            }
        }
    }
}
