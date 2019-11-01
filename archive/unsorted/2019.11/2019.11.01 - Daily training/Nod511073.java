package contest;

import template.FastInput;
import template.FastOutput;
import template.JosephusCircle;

public class Nod511073 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int ans = JosephusCircle.survivor(n, k);
        out.println(ans + 1);
    }
}
