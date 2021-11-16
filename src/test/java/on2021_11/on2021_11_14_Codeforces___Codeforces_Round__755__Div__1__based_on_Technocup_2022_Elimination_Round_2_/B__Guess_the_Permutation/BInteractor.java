package on2021_11.on2021_11_14_Codeforces___Codeforces_Round__755__Div__1__based_on_Technocup_2022_Elimination_Round_2_.B__Guess_the_Permutation;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;

public class BInteractor extends AbstractInteractor {
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        long n = input.ri();
        long l = input.ri();
        long m = input.ri();
        long r = input.ri();
        solutionInput.println(1).flush();
        solutionInput.println(n).flush();
        int remain = 40;
        while (true) {
            char c = solutionOutput.rc();
            if (c == '?') {
                remain--;
                if (remain < 0) {
                    solutionInput.println(-1);
                    return Verdict.WA;
                }
                int L = solutionOutput.readInt();
                int R = solutionOutput.readInt();
                long ans = inverse(l, m - 1, L, R) +
                        inverse(m, r, L, R);
                solutionInput.println(ans).flush();
            } else {
                int A = solutionOutput.ri();
                int B = solutionOutput.ri();
                int C = solutionOutput.ri();
                if (A != l || B != m || C != r) {
                    return Verdict.WA;
                } else {
                    return Verdict.OK;
                }
            }
        }
    }

    public long inverse(long l, long r, long L, long R) {
        l = Math.max(l, L);
        r = Math.min(r, R);
        if (l > r) {
            return 0;
        }
        long len = r - l + 1;
        return len * (len - 1) / 2;
    }
}
