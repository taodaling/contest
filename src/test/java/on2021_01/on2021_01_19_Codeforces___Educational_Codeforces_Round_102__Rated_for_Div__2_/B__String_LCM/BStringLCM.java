package on2021_01.on2021_01_19_Codeforces___Educational_Codeforces_Round_102__Rated_for_Div__2_.B__String_LCM;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LCMs;

public class BStringLCM {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String a = in.rs();
        String b = in.rs();
        int len = (int) LCMs.lcm(a.length(), b.length());
        StringBuilder aExt = new StringBuilder(len);
        StringBuilder bExt = new StringBuilder(len);
        for (int i = 0; i < len / a.length(); i++) {
            aExt.append(a);
        }
        for (int i = 0; i < len / b.length(); i++) {
            bExt.append(b);
        }
        if (aExt.toString().equals(bExt.toString())) {
            out.println(aExt);
        } else {
            out.println(-1);
        }

    }
}
