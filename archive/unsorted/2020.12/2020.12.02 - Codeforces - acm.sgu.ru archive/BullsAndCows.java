package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BullsAndCows {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String a = in.rs();
        String b = in.rs();
        int n = 0;
        int m = 0;
        for (int i = 0; i < 4; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                n++;
            } else {
                if(a.indexOf(b.charAt(i)) >= 0){
                    m++;
                }
            }
        }
        out.append(n).append(' ').append(m);
    }
}
