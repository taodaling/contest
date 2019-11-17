package on2019_11.on2019_11_16_AtCoder_Beginner_Contest_145.D___Knight;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();
        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        NumberTheory.Composite comp = new NumberTheory.Composite(1000000, mod);
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
