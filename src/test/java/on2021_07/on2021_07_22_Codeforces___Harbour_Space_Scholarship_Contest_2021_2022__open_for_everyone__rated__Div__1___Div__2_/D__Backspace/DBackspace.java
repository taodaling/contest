package on2021_07.on2021_07_22_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.D__Backspace;



import template.io.FastInput;
import template.io.FastOutput;

public class DBackspace {
    char[] s = new char[(int) 2e5];
    char[] t = new char[(int) 2e5];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        int m = in.rs(t);
        int iter = m - 1;
        int cnt = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (cnt == 1) {
                cnt ^= 1;
            } else if (iter >= 0 && t[iter] == s[i]) {
                iter--;
            } else {
                cnt ^= 1;
            }
        }
        out.println(iter < 0 ? "YES" : "NO");
    }
}
