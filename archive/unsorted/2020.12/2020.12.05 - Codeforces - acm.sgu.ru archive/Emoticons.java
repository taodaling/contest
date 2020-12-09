package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Emoticons {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int left = 0;
        int ans = 0;
        while(in.hasMore()){
            char c = in.rc();
            if (c == '(') {
                if (left == 0) {
                    left++;
                } else {
                    ans++;
                }
            } else if (c == ')') {
                if (left == 1) {
                    left--;
                } else {
                    ans++;
                }
            }else if(!(c == ' ' || Character.isAlphabetic(c))){
                if(left == 1){
                    left = 0;
                    ans++;
                }
            }
        }
        ans += left;
        out.println(ans);
    }
}
