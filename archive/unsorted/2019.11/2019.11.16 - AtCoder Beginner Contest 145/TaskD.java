package contest;

import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();
        Modular mod = new Modular(1e9 + 7);
        Composite comp = new Composite(1000000, mod);
        int ans = 0;
        for (int i = 0; i <= x; i++) {
            int rx = x - i;
            int ry = y - 2 * i;
            if(rx < 0 || ry < 0 || ry * 2 != rx){
                continue;
            }
            int j = ry;
            ans = mod.plus(ans, comp.composite(i + j, i));
        }

        out.println(ans);
    }
}
