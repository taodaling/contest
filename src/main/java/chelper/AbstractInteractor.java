package chelper;

import net.egork.chelper.tester.State;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractInteractor {
    public final Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput, State<Boolean> state) {
        return interact(new FastInput(input), new FastInput(solutionOutput),
                new FastOutput(solutionInput));
    }

    public abstract Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput);
}
