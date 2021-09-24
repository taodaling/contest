package on2021_09.on2021_09_12_Codeforces___Codeforces_Global_Round_16.B__MIN_MEX_Cut;



import template.io.FastInput;
import template.io.FastOutput;

public class BMINMEXCut {
    char[] s = new char[(int) 1e5];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        int[] cnt = new int[2];
        for (int i = 0; i < n; i++) {
            cnt[s[i] - '0']++;
        }
        if (cnt[0] == 0) {
            out.println(0);
            return;
        } else if (cnt[1] == 0) {
            out.println(1);
            return;
        }
        int block = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == '0' && (i == 0 || s[i - 1] != '0')) {
                block++;
            }
        }
        if(block == 1){
            out.println(1);
        }else{
            out.println(2);
        }
    }
}
