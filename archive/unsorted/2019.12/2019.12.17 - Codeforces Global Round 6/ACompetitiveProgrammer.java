package contest;



import template.io.FastInput;
import template.io.FastOutput;

public class ACompetitiveProgrammer {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int n = s.length;
        int zero = 0;
        int sum = 0;
        int even = 0;
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
            if (s[i] == 0) {
                zero++;
            }
            if (s[i] % 2 == 0) {
                even++;
            }
            sum += s[i];
        }

        boolean valid = zero > 0 && sum % 3 == 0 && even > 1;
        out.println(valid ? "red" : "cyan");
    }
}
