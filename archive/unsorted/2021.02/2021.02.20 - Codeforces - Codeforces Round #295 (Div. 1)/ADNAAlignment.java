package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

public class ADNAAlignment {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] cnts = new int[128];
        for(int i = 0; i < n; i++){
            cnts[in.rc()]++;
        }
        int max = 0;
        int maxCnt = 0;
        for(char c : "ACGT".toCharArray()){
            if(cnts[c] > max){
                max = cnts[c];
                maxCnt = 0;
            }
            if(cnts[c] == max){
                maxCnt++;
            }
        }

        int mod = (int)1e9 + 7;
        Power pow = new Power(mod);
        out.println(pow.pow(maxCnt, n));
    }
}
