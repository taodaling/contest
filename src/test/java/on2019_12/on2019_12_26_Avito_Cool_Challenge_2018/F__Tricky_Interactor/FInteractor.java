package on2019_12.on2019_12_26_Avito_Cool_Challenge_2018.F__Tricky_Interactor;



import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Hash;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class FInteractor {
    public Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput, State<Boolean> state) {
        FastInput stdin = new FastInput(input);
        FastInput solIn = new FastInput(solutionOutput);
        FastOutput solOut = new FastOutput(solutionInput);

        String s = stdin.readString();
        int n = s.length();
        int[] val = new int[n + 1];
        for (int i = 0; i < n; i++) {
            val[i + 1] = s.charAt(i) - '0';
        }
        int[] origin = val.clone();

        solOut.append(n).append(' ').append(sumOf(val)).println().flush();
        int time = 0;
        while (time < 10000) {
            time++;
            if (solIn.readChar() == '!') {
                return toString(val).equals(solIn.readString()) ? Verdict.OK : Verdict.WA;
            }
            int l = solIn.readInt();
            int r = solIn.readInt();
            if (!(1 <= l && l <= r && r <= n)) {
                return Verdict.WA;
            }
            Random random = new Random(hash(origin, l, r));
            if (random.nextBoolean()) {
                flip(val, 1, r);
            } else {
                flip(val, l, n);
            }
            solOut.println(sumOf(val)).flush();
            System.err.println(toString(val));
        }
        return Verdict.WA;
    }

    public int hash(int[] x, int l, int r) {
        long h = 1;
        for (int a : x) {
            h = (h * 31 + a) % ((long)1e9 + 7);
        }
        h = (h * 31 + l) % ((long)1e9 + 7);
        h = (h * 31 + r) % ((long)1e9 + 7);
        return (int)h;
    }

    public String toString(int[] x) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < x.length; i++) {
            builder.append(x[i]);
        }
        return builder.toString();
    }

    public int sumOf(int[] x) {
        int sum = 0;
        for (int a : x) {
            sum += a;
        }
        return sum;
    }

    public void flip(int[] x, int l, int r) {
        for (int i = l; i <= r; i++) {
            x[i] = 1 - x[i];
        }
    }
}
