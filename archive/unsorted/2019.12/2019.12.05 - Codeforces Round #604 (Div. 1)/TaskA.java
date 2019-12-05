package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }
        int g = 0;
        int s = 0;
        int b = 0;

        int goldVal = p[0];
        for (int i = 0; i < n && p[i] == goldVal; i++) {
            g++;
        }
        for (int i = g; i < n && (s <= g || p[i] == p[i - 1]); i++) {
            s++;
        }
        b = n / 2 - g - s;
        for (int i = g + s + b - 1; i >= 0 && p[i] == p[i + 1]; i--) {
            b--;
        }

        if (s <= g || b <= g || g <= 0 || s <= 0 || b <= 0) {
            s = b = g = 0;
        }
        out.append(g).append(' ').append(s).append(' ').append(b).append('\n');
    }
}
