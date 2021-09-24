package on2021_07.on2021_07_25_AtCoder___AtCoder_Regular_Contest_124.B___XOR_Matching_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.Randomized;

import java.util.Arrays;

public class BXORMatching2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        IntegerArrayList ans = new IntegerArrayList(n);
        int[] xorB = new int[n];
        for (int i = 0; i < n; i++) {
            int x = a[0] ^ b[i];
            for (int j = 0; j < n; j++) {
                xorB[j] = b[j] ^ x;
            }
            Randomized.shuffle(xorB);
            Arrays.sort(xorB);
            boolean equal = true;
            for (int j = 0; j < n; j++) {
                if (a[j] != xorB[j]) {
                    equal = false;
                }
            }
            if (equal) {
                ans.add(x);
            }
        }

        ans.unique();
        out.println(ans.size());
        for (int x : ans.toArray()) {
            out.println(x);
        }
    }
}
