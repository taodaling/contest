package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.Manacher;

public class LongestPalindrome {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        int[] odd = new int[n];
        int[] even = new int[n];
        Manacher.evenPalindrome(i -> s[i], n, even);
        Manacher.oddPalindrome(i -> s[i], n, odd);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(2 * odd[i] - 1, ans);
            ans = Math.max(2 * even[i], ans);
        }
        for (int i = 0; i < n; i++) {
            if (2 * odd[i] - 1 == ans) {
                int l = i - odd[i] + 1;
                int r = i + odd[i] - 1;
                for (int j = l; j <= r; j++) {
                    out.append(s[j]);
                }
                return;
            }
            if (even[i] * 2 == ans) {
                int l = i - even[i];
                int r = l + even[i] * 2 - 1;
                for(int j = l; j <= r; j++){
                    out.append(s[j]);
                }
                return;
            }
        }
    }
}
