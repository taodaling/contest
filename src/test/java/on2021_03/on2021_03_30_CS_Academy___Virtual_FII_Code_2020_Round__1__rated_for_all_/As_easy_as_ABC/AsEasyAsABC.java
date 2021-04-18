package on2021_03.on2021_03_30_CS_Academy___Virtual_FII_Code_2020_Round__1__rated_for_all_.As_easy_as_ABC;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.Random;

public class AsEasyAsABC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        int mid = a[n / 2];
        int over = 0;
        int deltaOne = 0;
        for (int x : a) {
            if (x >= mid + 1) {
                over++;
            } else if (x == mid) {
                deltaOne++;
            }
        }
        over += Math.min(deltaOne, k);
        if(over >= (n + 1) / 2){
            out.println(mid + 1);
        }else{
            out.println(mid);
        }
    }
}
