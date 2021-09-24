package on2021_09.on2021_09_03_CS_Academy___Virtual_Round__24.Kth_Special_Number;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class KthSpecialNumber {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        for (int i = 1; ; i++) {
            boolean ok = true;
            for (int j = 0; j < 30; j++) {
                int mask = 1 << j | 1 << j + 1;
                if((i & mask) == mask){
                    ok = false;
                    break;
                }
            }
            if(!ok){
                continue;
            }
            k--;
            if(k == 0){
                out.println(i);
                return;
            }
        }
    }
}
