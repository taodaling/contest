package on2021_07.on2021_07_22_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.C__Penalty;



import template.io.FastInput;
import template.io.FastOutput;

public class CPenalty {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        char[] rep0 = s.clone();
        char[] rep1 = s.clone();
        for (int i = 0; i < 10; i++) {
            if (s[i] == '?') {
                rep0[i] = (i & 1) == 0 ? '1' : '0';
                rep1[i] = (i & 1) == 1 ? '1' : '0';
            }
        }

        int ans0 = solve(rep0);
        int ans1 = solve(rep1);
        int min = Math.min(ans0, ans1);
        out.println(min + 1);
    }

    public int even(int n) {
        if (n < 0) {
            return 0;
        }
        return n / 2 + 1;
    }

    public int even(int l, int r) {
        return even(r) - even(l - 1);
    }

    public int odd(int l, int r) {
        return r - l + 1 - even(l, r);
    }

    public int solve(char[] s) {
        int[] score = new int[2];
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '1') {
                score[i & 1]++;
            }
            if (score[0] + even(i + 1, s.length - 1) < score[1]) {
                return i;
            }
            if (score[1] + odd(i + 1, s.length - 1) < score[0]) {
                return i;
            }
        }
        return 9;
    }
}
