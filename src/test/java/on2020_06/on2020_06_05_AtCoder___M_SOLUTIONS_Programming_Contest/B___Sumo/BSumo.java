package on2020_06.on2020_06_05_AtCoder___M_SOLUTIONS_Programming_Contest.B___Sumo;



import template.io.FastInput;
import template.io.FastOutput;

public class BSumo {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int ans = 15 - s.length;
        for(char c : s){
            if(c == 'o'){
                ans++;
            }
        }
        out.println(ans >= 8 ? "YES" : "NO");
    }
}
