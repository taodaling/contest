package contest;

import template.io.FastInput;
import java.io.PrintWriter;

public class Repetitions {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        char[] s = new char[(int)1e6];
        int n = in.readString(s, 0);
        int ans = 0;
        for(int i = 0; i < n; i++){
            int j = i;
            while(j + 1 < n && s[j + 1] == s[j]){
                j++;
            }
            ans = Math.max(ans, j - i + 1);
            i = j;
        }
        out.println(ans);
    }
}
