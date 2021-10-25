package on2021_10.on2021_10_22_Codeforces___Codeforces_Round__749__Div__1___Div__2__based_on_Technocup_2022_Elimination_Round_1_.D__Omkar_and_the_Meaning_of_Life;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DInteractor extends AbstractInteractor {
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.ri();
        int[] a = input.ri(n);
        int remainChance = 2 * n;
        solutionInput.println(n).flush();
        while (true) {
            char c = solutionOutput.rc();
            if (c == '?') {
                remainChance--;
                if (remainChance < 0) {
                    return Verdict.WA;
                }
                int[] x = a.clone();
                for (int i = 0; i < n; i++) {
                    x[i] += solutionOutput.readInt();
                }
                int find = -1;
                for (int i = 0; i < n && find == -1; i++) {
                    for (int j = i + 1; j < n && find == -1; j++) {
                        if (x[i] == x[j]) {
                            find = i;
                            break;
                        }
                    }
                }
                solutionInput.println(find + 1).flush();
            } else {
                int[] x = solutionOutput.ri(n);
                return Arrays.equals(a, x) ? Verdict.OK : Verdict.WA;
            }
        }
    }
}
