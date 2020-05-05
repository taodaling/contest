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
        int[] p = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = input.readInt();
            b[p[i]] = i;
        }


        solutionInput.println(n).flush();
        int ask = 0;
        while (true) {
            char c = solutionOutput.readChar();
            if (c == '?') {
                ask++;
                if (ask > n * 2) {
                    return Verdict.WA;
                }
                int i = solutionOutput.readInt();
                int j = solutionOutput.readInt();
                solutionInput.println(p[i] ^ b[j]).flush();
            } else {
                int way = solutionOutput.readInt();
                int[] oneWay = new int[n];
                int[] oneWayRev = new int[n];
                for (int i = 0; i < n; i++) {
                    oneWay[i] = solutionOutput.readInt();
                    oneWayRev[oneWay[i]] = i;
                }

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if ((p[i] ^ b[j]) != (oneWay[i] ^ oneWayRev[j])) {
                            return Verdict.WA;
                        }
                    }
                }

                return Verdict.OK;
            }
        }
    }
}
