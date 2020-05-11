package on2020_05.on2020_05_11_Luogu.P4279__SHOI2008_______;



import template.io.FastInput;
import template.io.FastOutput;

public class P4279SHOI2008 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int cnt = 0;
        int sg = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            sg ^= x;
            if (x > 1) {
                cnt++;
            }
        }

        String first = "John";
        String second = "Brother";
        if (cnt == 0) {
            out.println(sg == 0 ? first : second);
            return;
        }
        if (cnt == 1) {
            out.println(first);
            return;
        }
        out.println(sg == 0 ? second : first);
    }
}
