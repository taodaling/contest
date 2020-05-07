package on2020_05.on2020_05_07_Codeforces___Codeforces_Round__639__Div__1_.D__R_sum__Review1;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

public class DTokenChecker extends AbstractChecker {
    public DTokenChecker(String parameters) {
        super(parameters);
    }

    public long apply(long a, long b) {
        return b * (a - b * b);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.readInt();
        long k = stdin.readLong();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = stdin.readInt();
        }
        boolean valid = true;
        long total = 0;
        int[] b = new int[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            b[i] = actual.readInt();
            total += b[i];
            if (b[i] < 0 || b[i] > a[i]) {
                valid = false;
            }
            sum += apply(a[i], b[i]);
        }
        if (total != k) {
            valid = false;
        }
        long exp = expect.readLong();
        if (exp != sum) {
            valid = false;
        }
        if (!valid) {
            return Verdict.WA;
        }
        return Verdict.OK;
    }
}