package on2019_11.on2019_11_23_DISCO_Presents_Discovery_Channel_Code_Contest_2020_Qual.E___Majority_of_Balls;



import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class TaskEInteractor {
    public Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput, State<Boolean> state) {
        FastInput paramIn = new FastInput(input);
        FastInput solutionIn = new FastInput(solutionOutput);
        FastOutput solutionOut = new FastOutput(solutionInput);
        int n = paramIn.readInt();
        char[] value = new char[2 * n + 1];
        paramIn.readString(value, 1);
        solutionOut.println(n);
        solutionOut.flush();
        int invokeTime = 0;
        while (true) {
            char type = solutionIn.readChar();
            if (type == '?') {
                int cnt = 0;
                invokeTime++;
                if (invokeTime >= 211) {
                    return Verdict.WA;
                }
                Set<Integer> set = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    int x = solutionIn.readInt();
                    if (set.contains(x)) {
                        return Verdict.WA;
                    }
                    set.add(x);
                    cnt += value[x] == 'R' ? 1 : -1;
                }
                solutionOut.println(cnt > 0 ? "Red" : "Blue");
                solutionOut.flush();
            } else if (type == '!') {
                for (int i = 1; i <= 2 * n; i++) {
                    if (value[i] != solutionIn.readChar()) {
                        return Verdict.WA;
                    }
                }
                return Verdict.OK;
            } else {
                return Verdict.WA;
            }
        }
    }
}
