package on2020_05.on2020_05_08_.Ruler;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;

public class Ruler {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        n = Integer.highestOneBit(n);
        int x = in.readInt() % n;
        int ans = Integer.lowestOneBit(x + 1);
        out.println(ans);
    }
}
