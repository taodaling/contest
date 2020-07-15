package contest;

import java.util.Arrays;

public class FindStringHard {
    public String withAntipalindromicSubstrings(int n) {
        char[] ans = new char[100];
        Arrays.fill(ans, 'a');
        for (int i = 0; i < 100 && n > 0; i += 2) {
            int len = i + 2;
            if (n < len - 1) {
                break;
            }
            n -= len - 1;
            ans[i] = 'b';
        }
        for(int i = 99; n > 0; i--){
            ans[i] = 'b';
            n--;
        }

        return String.valueOf(ans);
    }
}
