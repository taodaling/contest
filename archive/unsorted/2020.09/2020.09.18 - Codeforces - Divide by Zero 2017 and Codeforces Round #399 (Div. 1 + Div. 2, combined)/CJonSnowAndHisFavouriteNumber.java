package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CJonSnowAndHisFavouriteNumber {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int x = in.readInt();
        int[] prev = new int[1 << 10];
        for (int i = 0; i < n; i++) {
            prev[in.readInt()]++;
        }
        int[] next = new int[1 << 10];
        for (int j = 0; j < k; j++) {
            Arrays.fill(next, 0);
            int ps = 0;
            for (int i = 0; i < 1 << 10; i++) {
                int transfer = (ps + prev[i] + 1) / 2 -
                        (ps + 1) / 2;
                ps += prev[i];
                next[i ^ x] += transfer;
                next[i] += prev[i] - transfer;
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }

        int min = -1;
        int max = -1;
        for(int i = 0; i < 1 << 10; i++){
            if(prev[i] == 0){
                continue;
            }
            if(min == -1){
                min = i;
            }
            max = i;
        }
        out.println(max).println(min);
    }

}
