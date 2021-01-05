package contest;


import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.problem.BinaryBitAssignProblem;

public class CPomanNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long t = in.rl();
        char[] s = new char[n];
        in.rs(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
        t -= 1 << s[n - 1];
        t += 1 << s[n - 2];
        long[] sets = new long[60];
        for (int i = 0; i < n - 2; i++) {
            sets[s[i]]++;
        }
        boolean possible = BinaryBitAssignProblem.assign2(sets, t);
        out.println(possible ? "Yes" : "No");
    }
}
