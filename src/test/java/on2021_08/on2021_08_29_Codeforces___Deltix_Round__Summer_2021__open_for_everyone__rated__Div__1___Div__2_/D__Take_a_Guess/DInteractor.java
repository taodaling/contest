package on2021_08.on2021_08_29_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.D__Take_a_Guess;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class DInteractor extends AbstractInteractor {
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.ri();
        int k = input.ri();
        int[] a = input.ri(n);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(a[i], a[j]), 0, n);
        int ans = a[indices[k - 1]];
        int op = 2 * n;
        solutionInput.append(n).append(' ').append(k).println().flush();
        while (op >= 0) {
            String s = solutionOutput.rs();
            if (s.equals("and")) {
                op--;
                int i = solutionOutput.ri() - 1;
                int j = solutionOutput.ri() - 1;
                solutionInput.println(a[i] & a[j]).flush();
            } else if (s.equals("or")) {
                op--;
                int i = solutionOutput.ri() - 1;
                int j = solutionOutput.ri() - 1;
                solutionInput.println(a[i] | a[j]).flush();
            } else {
                int res = solutionOutput.ri();
                return ans == res ? Verdict.OK : Verdict.WA;
            }
        }
        return Verdict.WA;
    }
}
