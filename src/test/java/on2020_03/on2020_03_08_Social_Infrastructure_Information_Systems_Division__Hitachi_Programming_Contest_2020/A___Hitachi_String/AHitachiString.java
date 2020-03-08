package on2020_03.on2020_03_08_Social_Infrastructure_Information_Systems_Division__Hitachi_Programming_Contest_2020.A___Hitachi_String;



import template.io.FastInput;
import template.io.FastOutput;

public class AHitachiString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        out.println(s.replaceAll("hi", "").length() == 0 ? "Yes" : "No");
    }
}
