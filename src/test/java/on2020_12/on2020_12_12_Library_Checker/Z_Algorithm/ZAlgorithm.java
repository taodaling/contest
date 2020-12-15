package on2020_12.on2020_12_12_Library_Checker.Z_Algorithm;



import template.io.FastInput;
import template.io.FastOutput;

public class ZAlgorithm {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int)5e5];
        int n = in.rs(s);
        int[] z = new int[n];
        template.string.ZAlgorithm.generate(z, i -> s[i], n);
        for(int x : z){
            out.append(x).append(' ');
        }
    }
}
