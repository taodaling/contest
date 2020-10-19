package contest;

import template.io.FastInput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class AAvoidingZero {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Randomized.shuffle(a);
        Arrays.sort(a);
        int sum = Arrays.stream(a).sum();
        if (sum == 0) {
            out.println("NO");
            return;
        }
        out.println("YES");
        if (sum > 0) {
            SequenceUtils.reverse(a);
        }
        for(int i = 0; i < n; i++){
            out.print(a[i]);
            out.append(' ');
        }
        out.println();
    }
}
