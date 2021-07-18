package on2021_07.on2021_07_16_DMOJ___Wesley_s_Anger_Contest_5.Problem_1___Matryoshka_Acorns;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class Problem1MatryoshkaAcorns {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        SequenceUtils.reverse(a);
        long ans = 0;
        int exist = 0;
        for(int i = 0; i < n; i++){
            int l = i;
            int r = i;
            while(r + 1 < n && a[r + 1] == a[i]){
                r++;
            }
            i = r;
            int num = r - l + 1;
            if(num > exist){
                ans += (long)(num - exist) * a[i];
                exist = num;
            }
        }
        out.println(ans);
    }
}
