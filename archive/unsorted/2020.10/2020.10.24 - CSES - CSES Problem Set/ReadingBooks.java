package contest;

import template.io.FastInput;
import template.rand.Randomized;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class ReadingBooks {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] t = new int[n];
        in.populate(t);
        long sum = Arrays.stream(t).mapToLong(Long::valueOf).sum();
        long max = Arrays.stream(t).max().orElse(-1);
        long ans = Math.max(max * 2, sum);
        out.println(ans);
    }
}
