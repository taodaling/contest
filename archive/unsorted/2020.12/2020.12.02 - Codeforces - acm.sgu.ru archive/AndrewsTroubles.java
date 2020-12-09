package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class AndrewsTroubles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int s = in.ri();
        int p = in.ri();
        if (p <= s) {
            out.println(0);
            return;
        }
        int late = p - s;
        late = late / 60;
        if(late >= 30){
            out.println(4);
            return;
        }
        if(late >= 15){
            out.println(3);
            return;
        }

        if(late >= 5){
            out.println(2);
            return;
        }
        out.println(1);
    }
}
