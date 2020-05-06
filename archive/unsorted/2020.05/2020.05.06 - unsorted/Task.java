package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.NimProduct;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readLong();
        long b = in.readLong();

        long ans = NimProduct.nimProduct(a, b);
        out.println(ans);
    }
}
