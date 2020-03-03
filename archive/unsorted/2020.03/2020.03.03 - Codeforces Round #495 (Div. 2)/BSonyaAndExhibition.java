package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BSonyaAndExhibition {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            out.append(i % 2);
        }
    }
}
