package contest;

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
