package on2021_08.on2021_08_29_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.A__A_Variety_of_Operations;



import template.io.FastInput;
import template.io.FastOutput;

public class AAVarietyOfOperations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int c = in.ri();
        int d = in.ri();
        if ((c & 1) != (d & 1)) {
            out.println(-1);
        } else {
            if (Math.abs(c) == Math.abs(d)) {
                out.println(c == 0 ? 0 : 1);
            } else {
                out.println(2);
            }
        }
    }
}
