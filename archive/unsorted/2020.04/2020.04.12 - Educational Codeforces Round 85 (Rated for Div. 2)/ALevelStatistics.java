package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ALevelStatistics {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int lastP = 0;
        int lastC = 0;
        boolean possible = true;
        for(int i = 0; i < n; i++){
            int p = in.readInt();
            int c = in.readInt();
            if(lastP > p || lastC > c || p - lastP < c - lastC){
                possible = false;
            }
            lastP = p;
            lastC = c;
        }
        out.println(possible ? "YES" : "NO");
    }
}
