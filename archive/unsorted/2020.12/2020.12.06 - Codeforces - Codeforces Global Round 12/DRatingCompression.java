package contest;

import dp.Lis;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DRatingCompression {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        List<Integer>[] indices = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            indices[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            indices[a[i]].add(i);
        }
        int l = 0;
        int r = n - 1;
        int inf = (int) 1e9;
        int[] atLeast = new int[n + 1];
        Arrays.fill(atLeast, inf);
        for (int i = 1; i <= n; i++) {
            int size = 0;
            int min = n;
            int max = -1;
            for (int j : indices[i]) {
                if (j < l || j > r) {
                    continue;
                }
                size++;
                min = Math.min(min, j);
                max = Math.max(max, j);
            }
            if (size == 1 && min == l) {
                l++;
                atLeast[i] = 0;
                continue;
            }
            if (size == 1 && max == r) {
                r--;
                atLeast[i] = 0;
                continue;
            }
            if(size == 1){
                atLeast[i] = r - l + 1;
                break;
            }
            if (size >= 2) {
                l++;
                r--;
                atLeast[i] = r - l + 1;
                break;
            }
             break;
        }
        int now = 0;
        boolean[] ok = new boolean[n + 1];
        for(int i = 1; i <= n; i++){
            now = Math.max(now, atLeast[i]);
            if(now <= n - i + 1){
                ok[i] = true;
            }
        }
        ok[n] = true;
        for(int i = 1; i <= n; i++){
            if(indices[i].isEmpty()){
                ok[n] = false;
            }
        }
        for(int i = n; i >= 1; i--){
            out.append(ok[i] ? '1' : '0');
        }
        out.println();
    }

}
