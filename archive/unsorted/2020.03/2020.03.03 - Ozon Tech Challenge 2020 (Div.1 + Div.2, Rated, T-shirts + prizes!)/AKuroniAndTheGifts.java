package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class AKuroniAndTheGifts {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        for(int i = 0; i < n; i++){
            b[i] = in.readInt();
        }
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        for(int i = 0; i < n; i++){
            out.append(a[i]).append(' ');
        }
        out.println();

        for(int i = 0; i < n; i++){
            out.append(b[i]).append(' ');
        }
        out.println();
    }
}
