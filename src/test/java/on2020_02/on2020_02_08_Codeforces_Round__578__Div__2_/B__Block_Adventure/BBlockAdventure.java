package on2020_02.on2020_02_08_Codeforces_Round__578__Div__2_.B__Block_Adventure;



import template.io.FastInput;
import template.io.FastOutput;

public class BBlockAdventure {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int lastH = in.readInt();
        boolean valid = true;
        for (int i = 1; i < n; i++) {
            int h = in.readInt();
            int atLeast = Math.max(0, h - k);
            if (atLeast > m + lastH) {
                valid = false;
            }
            m = lastH + m - atLeast;
            lastH = h;
        }
        out.println(valid ? "YES" : "NO");
    }
}
