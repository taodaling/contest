package contest;

import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeSet;

public class DInteractor extends AbstractInteractor {
    @Override
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        TreeSet<Integer> xSet = new TreeSet<>();
        TreeSet<Integer> ySet = new TreeSet<>();
        int n = input.readInt();
        int m = input.readInt();
        for (int i = 0; i < n; i++) {
            xSet.add(input.readInt());
        }
        for (int j = 0; j < m; j++) {
            ySet.add(input.readInt());
        }
        int limit = 300001;
        while (limit-- > 0) {
            int type = solutionOutput.readInt();
            if (type == 0) {
                int x = solutionOutput.readInt();
                int y = solutionOutput.readInt();

                if (Math.abs(x) > 1e8 || Math.abs(y) > 1e8) {
                    return Verdict.WA;
                }

                int ans = (int) 1e9;
                if (xSet.ceiling(x) != null) {
                    ans = Math.min(ans, xSet.ceiling(x) - x);
                }
                if (ySet.ceiling(y) != null) {
                    ans = Math.min(ans, ySet.ceiling(y) - y);
                }
                if (xSet.floor(x) != null) {
                    ans = Math.min(ans, x - xSet.floor(x));
                }
                if (ySet.floor(y) != null) {
                    ans = Math.min(ans, y - ySet.floor(y));
                }

                solutionInput.println(ans).flush();
            } else if (type == 1) {
                if (solutionOutput.readInt() != n || solutionOutput.readInt() != m) {
                    return Verdict.WA;
                }
                for (int i = 0; i < n; i++) {
                    int x = solutionOutput.readInt();
                    xSet.remove(x);
                }
                for (int i = 0; i < m; i++) {
                    int y = solutionOutput.readInt();
                    ySet.remove(y);
                }
                return xSet.isEmpty() && ySet.isEmpty() ? Verdict.OK : Verdict.WA;
            } else {
                return Verdict.WA;
            }
        }
        return Verdict.WA;
    }
}
