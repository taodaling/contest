package contest;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import net.egork.chelper.tester.State;
import net.egork.chelper.tester.Verdict;
import template.FastInput;

public class TaskInteractor {
    public Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput,
                    State<Boolean> state) {
        FastInput in = new FastInput(input);
        FastInput so = new FastInput(solutionOutput);
        PrintWriter pw = new PrintWriter(solutionInput);

        int k = in.readInt();
        pw.println(k);
        pw.flush();
        int x = 9;
        for (int i = 0; i < k; i++) {
            x = x * 2 + 1;
            pw.println(x);
            pw.flush();
            if (!(so.hasMore() && so.readInt() > x)) {
                return Verdict.WA;
            }
        }

        return Verdict.OK;
    }
}
