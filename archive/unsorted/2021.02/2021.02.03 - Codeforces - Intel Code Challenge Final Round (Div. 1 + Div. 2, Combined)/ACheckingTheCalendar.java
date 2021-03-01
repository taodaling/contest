package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ACheckingTheCalendar {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String[] day = new String[]{
                "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
        };
        int a = SequenceUtils.indexOf(day, 0, 6, in.rs());
        int b = SequenceUtils.indexOf(day, 0, 6, in.rs());
        int[] cnts = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int i = 0; i + 1 < cnts.length; i++) {
            int x = (a + cnts[i]) % 7;
            if(x == b){
                out.println("YES");
                return;
            }
        }
        out.println("NO");
    }
}
