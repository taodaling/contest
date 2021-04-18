package on2021_03.on2021_03_25_Codeforces___Coder_Strike_2014___Finals__online_edition__Div__1_.A__Start_Up;



import template.io.FastInput;
import template.io.FastOutput;

public class AStartUp {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        boolean[] isMirror = new boolean[128];
        for (char x : new char[]{'A', 'H', 'I', 'M', 'O', 'T', 'U', 'V', 'W', 'X', 'Y'}) {
            isMirror[x] = true;
        }
        char[] s = in.rs().toCharArray();
        for (char x : s) {
            if (!isMirror[x]) {
                out.println("NO");
                return;
            }
        }
        int l = 0;
        int r = s.length - 1;
        while (l < r) {
            if (s[l] != s[r]) {
                out.println("NO");
                return;
            }
            l++;
            r--;
        }
        out.println("YES");
    }
}
