package on2019_12.on2019_12_22_AtCoder_Beginner_Contest_148.B___Strings_with_the_Same_Length;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();
        char[] t = in.readString().toCharArray();
        for(int i = 0; i < n; i++){
            out.append(s[i]).append(t[i]);
        }
    }
}
