package on2021_06.on2021_06_27_AtCoder___AtCoder_Grand_Contest_054.A___Remove_Substrings;



import template.io.FastInput;
import template.io.FastOutput;

public class ARemoveSubstrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        if(s[0] != s[n - 1]){
            out.println(1);
            return;
        }
        for(int i = 1; i < n; i++){
            if(s[i] != s[0] && s[i - 1] != s[0]){
                out.println(2);
                return;
            }
        }
        out.println(-1);
    }
}
