package on2020_02.on2020_02_29_CodeChef___February_Lunchtime_2020_Division_2.Swapping_to_Palindrome;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class SwappingToPalindrome {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        int l = 0;
        int r = n - 1;
        int req = 0;
        boolean[] froze = new boolean[n];
        while (l < r) {
            if (s[l] != s[r]) {
                if (r - 1 != l && s[l] == s[r - 1] && !froze[r] && !froze[r - 1]) {
                    SequenceUtils.swap(s, r, r - 1);
                    froze[r] = froze[r - 1] = true;
                    req++;
                } else if (l + 1 != r && s[l + 1] == s[r] && !froze[l] && !froze[l + 1]) {
                    SequenceUtils.swap(s, l, l + 1);
                    froze[l] = froze[l + 1] = true;
                    req++;
                } else {
                    out.println("NO");
                    return;
                }
            }
            l++;
            r--;
        }

        out.println("YES");
        out.println(req);
    }
}
