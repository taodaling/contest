package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class OlympiadForEveryone {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);

        int p = in.readInt();

        long way = 1;
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        for (int i = 0, l = 0; i < n; i++) {
            while (l < n && b[l] <= a[i]) {
                l++;
            }
            int remain = l - i;
            way = way * remain % p;
        }
        out.println(way);
    }
}
