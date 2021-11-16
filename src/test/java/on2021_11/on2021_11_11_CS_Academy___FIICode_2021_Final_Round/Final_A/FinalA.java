package on2021_11.on2021_11_11_CS_Academy___FIICode_2021_Final_Round.Final_A;



import template.binary.FastBitCount2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;
import template.math.LongMillerRabin;
import template.primitve.generated.datastructure.LongMinQueue;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.function.LongPredicate;

public class FinalA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long ans = 0;
        for(int i = 0; i < n; i++){
            long x = in.rl();
            x *= 2;
            if(FastBitCount2.count(x) > 1){
                ans++;
            }
        }
        out.println(ans);
    }
}
