package contest;

import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.InputStream;
import java.io.OutputStream;

public class BInteractor extends AbstractInteractor {
    @Override
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.readInt();
        for (int i = 0; i < n; i++) {
            int a = input.readInt();
            int used = 0;
            solutionInput.println("start").flush();
            while (true) {
                char c = solutionOutput.readChar();
                if (c == '?') {
                    used++;
                    if (used > 60) {
                        solutionInput.println("e").flush();
                        return Verdict.WA;
                    }
                    int x = solutionOutput.readInt();
                    int y = solutionOutput.readInt();
                    if (x < 0 || y < 0 || x > 2e9 || y > 2e9) {
                        solutionInput.println("e").flush();
                        return Verdict.WA;
                    }
                    solutionInput.println(x % a >= y % a ? "x" : "y").flush();
                } else {
                    int ans = solutionOutput.readInt();
                    if (ans != a) {
                        solutionInput.println("mistake").flush();
                        return Verdict.WA;
                    }
                    break;
                }
            }
        }
        solutionInput.println("end").flush();
        return Verdict.OK;
    }
}
