package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BVersePattern {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        boolean[] ok = new boolean[128];
        ok['a'] = true;
        ok['e'] = true;
        ok['i'] = true;
        ok['o'] = true;
        ok['y'] = true;
        ok['u'] = true;

        for(int x : a){
            int cnt = 0;
            in.skipBlank();
            for(char c : in.readLine().toCharArray()){
                if(ok[c]){
                    cnt++;
                }
            }
            if(cnt == x){
                continue;
            }
            out.println("NO");
            return;
        }
        out.println("YES");
    }
}
