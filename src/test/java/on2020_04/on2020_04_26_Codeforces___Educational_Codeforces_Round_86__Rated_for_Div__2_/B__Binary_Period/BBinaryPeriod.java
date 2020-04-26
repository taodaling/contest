package on2020_04.on2020_04_26_Codeforces___Educational_Codeforces_Round_86__Rated_for_Div__2_.B__Binary_Period;



import template.io.FastInput;
import template.io.FastOutput;

public class BBinaryPeriod {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int[] cnt = new int[2];
        for (char c : s) {
            cnt[c - '0']++;
        }
        if (cnt[0] > 0 && cnt[1] > 0) {
            for (int i = 0; i < s.length; i++) {
                out.append("01");
            }
        } else {
            for (char c : s) {
                out.append(c);
            }
        }
        out.println();
    }
}
