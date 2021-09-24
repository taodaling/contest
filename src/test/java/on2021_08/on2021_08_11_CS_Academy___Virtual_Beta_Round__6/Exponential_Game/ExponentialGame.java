package on2021_08.on2021_08_11_CS_Academy___Virtual_Beta_Round__6.Exponential_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.rand.Randomized;

import java.util.Arrays;

public class ExponentialGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        for(int i = 0; i < n; i++){
            int l = i;
            int r = i;
            while(r + 1 < n && a[r + 1] == a[i]){
                r++;
            }
            i = r;
            if((r - l + 1) % 2 == 1){
                out.println("A");
                return;
            }
        }
        out.println("B");
    }
}
