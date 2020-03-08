package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AHitachiString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        out.println(s.replaceAll("hi", "").length() == 0 ? "Yes" : "No");
    }
}
