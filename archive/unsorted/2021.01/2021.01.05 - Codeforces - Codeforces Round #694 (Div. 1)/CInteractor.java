package contest;

import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CInteractor extends AbstractInteractor {
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.readInt();
        int k = input.readInt();
        int thief = input.readInt() - 1;

        solutionInput.append(n).append(' ').append(k).println().flush();
        long[] cards = new long[n];
        Arrays.fill(cards, k);
        while (solutionOutput.hasMore()) {
            char c = solutionOutput.rc();
            int index = solutionOutput.ri();
            if (c == '!') {
                return thief + 1 == index ? Verdict.OK : Verdict.WA;
            }
            solutionInput.println(cards[index - 1]).flush();
            long[] next = new long[n];
            for (int j = 0; j < n; j++) {
                if (j == thief) {
                    next[(j + 1) % n] += cards[j];
                    continue;
                }
                long half = cards[j] / 2;
                next[(j + n - 1) % n] += half;
                next[(j + 1) % n] += cards[j] - half;
            }
            cards = next;
            //System.err.println(Arrays.toString(cards));
        }

        return Verdict.WA;
    }
}
