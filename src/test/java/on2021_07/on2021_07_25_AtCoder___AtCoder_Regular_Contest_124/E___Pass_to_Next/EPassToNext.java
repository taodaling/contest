package on2021_07.on2021_07_25_AtCoder___AtCoder_Regular_Contest_124.E___Pass_to_Next;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class EPassToNext {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Debug debug = new Debug(true);
        int[] seq = new int[]{1, 1, 1};
        int cnt = 0;
        for (int i = 0; i <= 3; i++) {
            if(i > seq[0] + seq[2]){
                continue;
            }
            for (int j = 0; j <= 3; j++) {
                if(j > seq[0] + seq[1]){
                    continue;
                }
                if (i <= seq[2] + seq[0] && i >= 0) {
                    if (i + j <= seq[2] + seq[0] + seq[1] && i + j >= seq[0]) {
                        cnt++;
                        debug.debugArray("maybe", new int[]{i, j, 3 - i - j});
                    }
                }
            }
        }
        out.println(cnt);
    }
}
