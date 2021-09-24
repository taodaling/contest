package on2021_07.on2021_07_22_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.B__Reverse_String;



import template.io.FastInput;
import template.io.FastOutput;

public class BReverseString {
    char[] s = new char[(int) 1e3];
    char[] t = new char[(int) 1e3];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        int m = in.rs(t);
        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                if (r - l + 1 > m) {
                    continue;
                }
                boolean ok = true;
                int rpos = 0;
                for (int i = l; i <= r && ok; i++) {
                    if (s[i] != t[rpos++]) {
                        ok = false;
                    }
                }
                if (!ok) {
                    continue;
                }
                for (int i = r - 1; i >= 0 && rpos < m && ok; i--) {
                    if (s[i] != t[rpos++]) {
                        ok = false;
                    }
                }
                if (!ok || rpos < m) {
                    continue;
                }
                out.println("YES");
                return;
            }
        }
        out.println("NO");
    }
}
