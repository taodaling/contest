package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class PluralFormOfNouns {
    String[] t1 = new String[]{"ch", "x", "s", "o"};

    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.rs();
        for (String x : t1) {
            if (s.endsWith(x)) {
                out.println(s + "es");
                return;
            }
        }

        if (s.endsWith("f")) {
            s = s.substring(0, s.length() - 1) + "ves";
            out.println(s);
            return;
        }
        if (s.endsWith("fe")) {
            s = s.substring(0, s.length() - 2) + "ves";
            out.println(s);
            return;
        }
        if(s.endsWith("y")){
            s = s.substring(0, s.length() - 1) + "ies";
            out.println(s);
            return;
        }
        s = s + "s";
        out.println(s);
    }
}
