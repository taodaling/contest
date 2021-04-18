package on2021_04.on2021_04_09_Codeforces___Codeforces_Round__215__Div__1_.A__Sereja_and_Algorithm;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

import java.util.Arrays;

public class ASerejaAndAlgorithm {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        for (int i = 0; i < n; i++) {
            s[i] -= 'x';
        }
        int m = in.ri();
        IntegerPreSum[] ps = new IntegerPreSum[3];
        for (int j = 0; j < 3; j++) {
            int finalJ = j;
            ps[j] = new IntegerPreSum(i -> s[i] == finalJ ? 1 : 0, n);
        }
        int[] cnts = new int[3];
        for (int i = 0; i < m; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            for(int j = 0; j < 3; j++){
                cnts[j] = ps[j].intervalSum(l, r);
            }
            Arrays.sort(cnts);
            if(r - l + 1 < 3 || cnts[2] - cnts[0] <= 1){
                out.println("YES");
            }else{
                out.println("NO");
            }
        }
    }
}
