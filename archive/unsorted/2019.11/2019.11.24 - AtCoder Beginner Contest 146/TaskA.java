package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        int ans = 0;
        if(s.equals("SUN")){
            ans = 7;
        }
        if(s.equals("MON")){
            ans = 6;
        }
        if(s.equals("TUE")){
            ans = 5;
        }
        if(s.equals("WED")){
            ans = 4;
        }
        if(s.equals("THU")){
            ans = 3;
        }
        if(s.equals("FRI")){
            ans = 2;
        }
        if(s.equals("SAT")){
            ans = 1;
        }
        out.println(ans);
    }
}
