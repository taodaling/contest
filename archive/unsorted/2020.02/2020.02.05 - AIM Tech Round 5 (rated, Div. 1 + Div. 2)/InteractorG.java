package contest;

import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class InteractorG extends AbstractInteractor {
    @Override
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) {
        long x = input.readLong();
        long M = 10004205361450474L;
        for (int i = 0; i < 5; i++) {
            int k = solutionOutput.readInt();
            if (k > 10000 || k == 0) {
                return Verdict.WA;
            }
            long[] interval = new long[k];
            for (int j = 0; j < k; j++) {
                interval[j] = solutionOutput.readLong();
            }
            boolean valid = interval[0] >= 1 && interval[k - 1] <= M &&
                    SortUtils.strictAscending(interval, 0, interval.length - 1);
            if (!valid) {
                return Verdict.WA;
            }
            for (int j = 0; j <= k; j++) {
                if (j == k) {
                    solutionInput.println(k).flush();
                    break;
                }
                if (interval[j] == x) {
                    solutionInput.println(-1).flush();
                    return Verdict.OK;
                }
                if (interval[j] > x) {
                    solutionInput.println(j).flush();
                    break;
                }
            }
        }

        solutionInput.println(-2);
        return Verdict.WA;
    }
}
