package on2019_10.on2019_10_16_Atcoder_codefestival_2016_qual_C.TaskA;



import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        int cIndex = s.indexOf('C');
        int fIndex = s.lastIndexOf('F');
        if (cIndex < 0 || fIndex < 0 || fIndex < cIndex) {
            out.println("No");
        } else {
            out.println("Yes");
        }
    }
}
