package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.Arrays;

public class NumbersPainting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] color = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int cnt = 0;
            int v = i;
            for (int j = 2; j * j <= i; j++) {
                if (i % j != 0) {
                    continue;
                }
                while(v % j == 0){
                    v /= j;
                    cnt++;
                }
            }
            if(v > 1){
                cnt++;
            }
            color[i] = cnt + 1;
        }
        int max = Arrays.stream(color).max().orElse(-1);
        out.println(max);
        for (int i = 1; i <= n; i++) {
            out.append(color[i]).append(' ');
        }

    }
}
