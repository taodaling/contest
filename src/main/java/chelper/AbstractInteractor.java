package chelper;

import net.egork.chelper.tester.State;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractInteractor {
    public final Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput, State<Boolean> state) throws Throwable {
        try {
            return interact(new FastInput(input), new FastInput(solutionOutput),
                    new FastOutput(solutionInput));
        } catch (Throwable t) {
            t.printStackTrace();
            return Verdict.UNDECIDED;
        }
    }

    public abstract Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable;
}
