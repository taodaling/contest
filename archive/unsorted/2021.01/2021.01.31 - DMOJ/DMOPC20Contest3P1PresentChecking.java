package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DMOPC20Contest3P1PresentChecking {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] cnts = new int[n];
        for (int i = 0; i < n; i++) {
            cnts[in.ri() - 1]++;
        }
        for(int i = 0; i < n; i++){
            if(cnts[i] == 0){
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}
