package contest;

import java.io.InputStream;
import java.io.OutputStream;

import net.egork.chelper.tester.State;
import net.egork.chelper.tester.Verdict;
import template.FastInput;
import template.FastOutput;

public class TaskAInteractor {
    public Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput,
                    State<Boolean> state) {
        FastInput in = new FastInput(input);
        FastInput so = new FastInput(solutionOutput);
        FastOutput out = new FastOutput(solutionInput);

        long N = in.readInt();
        int time = 0;
        while (true) {
            time++;
            char c = so.readChar();
            if (c == '?') {
                if (time > 64) {
                    return Verdict.WA;
                }
                long n = so.readLong();
                if (n <= N && Long.toString(n).compareTo(Long.toString(N)) <= 0
                                || n > N && Long.toString(n).compareTo(Long.toString(N)) > 0) {
                    out.println("Y");
                } else {
                    out.println("N");
                }
                out.flush();
            } else {
                long n = so.readLong();
                return n == N ? Verdict.OK : Verdict.WA;
            }
        }
    }
}
