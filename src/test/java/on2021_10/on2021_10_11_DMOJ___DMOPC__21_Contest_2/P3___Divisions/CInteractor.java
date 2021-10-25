package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P3___Divisions;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;

public class CInteractor extends AbstractInteractor {
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.ri();
        long[] a = input.rl(n);
        int limit = 32 * n;
        int cur = 0;
        solutionInput.println(n).flush();
        while (limit > 0 && cur < a.length) {
            limit--;
            long x = solutionOutput.rl();
            int ans = req(a[cur], x);
            if (ans == 0) {
                cur++;
            }
            solutionInput.println(ans).flush();
        }
        return cur < a.length ? Verdict.WA : Verdict.OK;
    }

    public int step(long a) {
        if (a == 0) {
            return 0;
        }
        return step(a / 2) + 1;
    }

    public int req(long a, long b) {
        if (a == b) {
            return 0;
        }
        if (a == 0) {
            return step(b);
        }
        if (b == 0) {
            return step(a);
        }
        String sa = Long.toBinaryString(a);
        String sb = Long.toBinaryString(b);
        int len = sa.length() + sb.length();
        for (int i = 0; i < sa.length() && i < sb.length(); i++) {
            if (sa.charAt(i) != sb.charAt(i)) {
                break;
            }
            len -= 2;
        }
        return len;
    }
}
