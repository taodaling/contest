package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AUnusualCompetitions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        int invalid = 0;
        int cnt = 0;
        for(int i = 0; i < n; i++){
            int prev = cnt;
            cnt += s[i] == '(' ? 1 : -1;
            if(prev < 0 && cnt <= 0 || (prev == 0 && cnt < 0)){
                invalid++;
            }
        }
        if(cnt != 0){
            out.println(-1);
            return;
        }
        out.println(invalid);
    }
}
