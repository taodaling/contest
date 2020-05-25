package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ABankRobbery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int n = in.readInt();
        int[] data = new int[n];
        in.populate(data);
        long ans = 0;
        for (int x : data) {
            if (x > b && x < c) {
                ans++;
            }
        }
        out.println(ans);
    }
}
