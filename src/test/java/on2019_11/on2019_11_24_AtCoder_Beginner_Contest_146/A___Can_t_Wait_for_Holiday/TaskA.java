package on2019_11.on2019_11_24_AtCoder_Beginner_Contest_146.A___Can_t_Wait_for_Holiday;



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
