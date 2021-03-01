package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EuclidLikeFunction;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        int c = in.ri();
        long f = EuclidLikeFunction.f(n, a, b, c);
        long g = EuclidLikeFunction.g(n, a, b, c);
        long h = EuclidLikeFunction.h(n, a, b, c);
        out.println(f);
        out.println(g);
        out.println(h);
    }
}
