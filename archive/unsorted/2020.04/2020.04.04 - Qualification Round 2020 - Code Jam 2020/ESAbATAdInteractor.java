package contest;

import chelper.AbstractInteractor;
import chelper.BindInOutStream;
import chelper.ExternalInterator;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ESAbATAdInteractor {
    public Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput, State<Boolean> state) throws Throwable {
        int set = new FastInput(input).readInt();
        ExternalInterator interator = new ExternalInterator(new String[]{"python", "C:\\Users\\DALT\\Desktop\\interactor.py", "" + set});

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new BindInOutStream(interator.getIn(), solutionInput));
        service.submit(new BindInOutStream(solutionOutput, interator.getOut()));
        service.shutdown();
        int status = interator.getExitCode();
        return status == 0 ? Verdict.OK : Verdict.WA;
    }
}
