package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        boolean[] valid = new boolean[128];

        int n = s.length;
        for(int i = 0; i < n; i++){
            int j = i;
            while(j + 1 < n && s[j + 1] == s[i]){
                j++;
            }
            if((j - i + 1) % 2 == 1){
                valid[s[i]] = true;
            }
            i = j;
        }

        for(int i = 'a'; i <= 'z'; i++){
            if(valid[i]){
                out.append((char)i);
            }
        }

        out.println();
    }
}
