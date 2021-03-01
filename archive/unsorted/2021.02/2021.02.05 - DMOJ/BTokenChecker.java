package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.math.GCDs;

public class BTokenChecker extends AbstractChecker {
    public BTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int k = stdin.ri();
        int n = actual.readInt();
        int[] data = actual.ri(n);
        if(n <= 0 || n > 100){
            return Verdict.WA;
        }
        for (int i = 0; i < n; i++) {
            if (data[i] < 1 || data[i] > 1e9) {
                return Verdict.WA;
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int g = 0;
            for (int j = i; j < n; j++) {
                g = GCDs.gcd(g, data[j]);

                if (g == 1) {
                    ans++;
                }
            }
        }
        return ans == k ? Verdict.OK : Verdict.WA;
    }
}