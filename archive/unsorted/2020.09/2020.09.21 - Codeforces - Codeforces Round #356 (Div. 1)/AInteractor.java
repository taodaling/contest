package contest;

import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.InputStream;
import java.io.OutputStream;

public class AInteractor extends AbstractInteractor {
    @Override
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int target = input.readInt();
        int cnt = 0;
        boolean prime = true;
        for (int i = 2; i * i <= target; i++) {
            if (target % i == 0) {
                prime = false;
                break;
            }
        }
        while (true) {
            String ask = solutionOutput.readString();
            if (ask.equals("prime")) {
                return prime ? Verdict.OK : Verdict.WA;
            }
            if (ask.equals("composite")) {
                return !prime ? Verdict.OK : Verdict.WA;
            }
            cnt++;
            if (cnt > 20) {
                return Verdict.WA;
            }
            int x = Integer.parseInt(ask);
            solutionInput.println(target % x == 0 ? "yes" : "no").flush();
        }
    }
}
