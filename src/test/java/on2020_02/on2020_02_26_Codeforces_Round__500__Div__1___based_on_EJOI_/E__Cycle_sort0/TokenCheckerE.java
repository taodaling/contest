package on2020_02.on2020_02_26_Codeforces_Round__500__Div__1___based_on_EJOI_.E__Cycle_sort0;





import net.egork.chelper.tester.StringInputStream;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;
import template.utils.CompareUtils;

public class TokenCheckerE implements Checker {
    public TokenCheckerE(String parameters) {
    }

    public Verdict check(String input, String expectedOutput, String actualOutput) {
        FastInput in = new FastInput(new StringInputStream(input));
        FastInput ai = new FastInput(new StringInputStream(actualOutput));
        int n = in.readInt();
        int s = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        int t = ai.readInt();
        for(int i = 0; i < t; i++){
            int k = ai.readInt();
            s -= k;
            if(s < 0){
                return Verdict.WA;
            }
            int[] q = new int[k];
            for(int j = 0; j < k; j++){
                q[j] = ai.readInt();
            }
            apply(a, q);
        }

        boolean ans = CompareUtils.notStrictAscending(a, 0, a.length - 1);
        return ans ? Verdict.OK : Verdict.WA;
    }

    public void apply(int[] a, int[] q) {
        int last = q[0];
        for (int i = 1; i < q.length; i++) {
            a[q[i]] = a[last];
            last = q[i];
        }
        a[q[0]] = a[last];
    }
}
