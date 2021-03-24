package on2021_03.on2021_03_22_Codeforces___Codeforces_Round__243__Div__1_.A__Sereja_and_Swaps;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ASerejaAndSwaps {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        int[] cover = new int[n];
        int[] notCover = new int[n];
        int best = a[0];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int coverTail = 0;
                int notCoverTail = 0;
                for (int t = 0; t < n; t++) {
                    if (t >= i && t <= j) {
                        cover[coverTail++] = a[t];
                    } else {
                        notCover[notCoverTail++] = a[t];
                    }
                }
                Randomized.shuffle(cover, 0, coverTail);
                Randomized.shuffle(notCover, 0, notCoverTail);
                Arrays.sort(cover, 0, coverTail);
                Arrays.sort(notCover, 0, notCoverTail);
                SequenceUtils.reverse(notCover, 0, notCoverTail - 1);
                for(int t = 0; t < k && t < coverTail && t < notCoverTail; t++){
                    if(notCover[t] > cover[t]){
                        cover[t] = notCover[t];
                    }
                }
                int sum = 0;
                for(int t = 0; t < coverTail; t++){
                    sum += cover[t];
                }
                best = Math.max(best, sum);
            }
        }
        out.println(best);
    }
}
