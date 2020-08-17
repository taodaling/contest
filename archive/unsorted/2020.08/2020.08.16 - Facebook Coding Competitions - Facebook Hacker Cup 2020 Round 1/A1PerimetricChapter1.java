package contest;

import template.datastructure.MinQueue;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.Debug;

public class A1PerimetricChapter1 {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.debug("testNumber", testNumber);
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        int k = in.readInt();
        int w = in.readInt();
        long[] L = new long[n + 1];
        long[] H = new long[n + 1];

        for (int i = 1; i <= k; i++) {
            L[i] = in.readInt();
        }

        long al = in.readInt();
        long bl = in.readInt();
        long cl = in.readInt();
        long dl = in.readInt();

        for (int i = 1; i <= k; i++) {
            H[i] = in.readInt();
        }

        long ah = in.readInt();
        long bh = in.readInt();
        long ch = in.readInt();
        long dh = in.readInt();

        for (int i = k + 1; i <= n; i++) {
            L[i] = (al * L[i - 2] + bl * L[i - 1] + cl) % dl + 1;
            H[i] = (ah * H[i - 2] + bh * H[i - 1] + ch) % dh + 1;
        }

        Modular mod = new Modular(1e9 + 7);
        int prod = 1;
        long perimeter = 0;
        MinQueue<long[]> queue = new MinQueue<>(n, (a, b) -> -Long.compare(a[0], b[0]));
        for (int i = 1; i <= n; i++) {
            while (!queue.isEmpty() && queue.first()[1] < L[i]) {
                queue.removeFirst();
            }
            long largest = queue.isEmpty() ? 0 : queue.query()[0];
            long last = queue.isEmpty() ? L[i] : queue.last()[1];
            if (largest < H[i]) {
                perimeter += 2L * (H[i] - largest);
            }
            perimeter += 2L * (L[i] + w - last);
            perimeter = mod.valueOf(perimeter);
            queue.add(new long[]{H[i], L[i] + w});
            prod = mod.mul(prod, perimeter);
        }

        out.println(prod);
    }
}