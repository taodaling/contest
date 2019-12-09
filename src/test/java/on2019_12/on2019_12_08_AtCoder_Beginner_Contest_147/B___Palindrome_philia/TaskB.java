package on2019_12.on2019_12_08_AtCoder_Beginner_Contest_147.B___Palindrome_philia;





import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        int l = 0;
        int r = s.length() - 1;
        int ans = 0;
        while(l < r){
            if(s.charAt(l) != s.charAt(r)){
                ans++;
            }
            l++;
            r--;
        }
        out.println(ans);
    }
}
