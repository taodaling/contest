package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CTwoBrackets {
    char[] s = new char[(int)2e5];
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readString(s, 0);
        int a = 0;
        int b = 0;
        int ans = 0;
        for(int i = 0; i < n; i++){
            char c = s[i];
            if(c == '('){
                a++;
            }
            if(c == '[') {
                b++;
            }
            if(c == ')' && a > 0){
                a--;
                ans++;
            }
            if(c == ']' && b > 0){
                b--;
                ans++;
            }
        }
        out.println(ans);
    }
}
