package on2019_12.on2019_12_01_Codeforces_Round__583__Div__1___Div__2__based_on_Olympiad_of_Metropolises_.F___Employment;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitBase;
import template.math.DigitUtils;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt() - 1;
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.readInt() - 1;
        }

        Randomized.randomizedArray(a, 0, n);
        Randomized.randomizedArray(b, 0, n);
        Arrays.sort(a);
        Arrays.sort(b);

        //posSum - canSum is answer
        long posSum = 0;
        long canSum = 0;
        for (int i = 0; i < n; i++) {
            posSum += b[i];
            canSum += a[i];
        }

       
    }

    int cwDist(int x, int y, int m) {
        return DigitUtils.mod(y - x, m);
    }


    int ccDist(int x, int y, int m) {
        return DigitUtils.mod(x - y, m);
    }
}
