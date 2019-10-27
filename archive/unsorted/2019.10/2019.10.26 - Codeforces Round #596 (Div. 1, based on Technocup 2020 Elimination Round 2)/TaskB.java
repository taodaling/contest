package contest;

import template.*;

import java.util.HashMap;
import java.util.Map;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];


        long limit = (long) 1e5;
        Map<Integer, Integer> cntMap = new HashMap<>(n);
        Map<Integer, Integer> invMap = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            int v = in.readInt();
            int built = 1;
            long inv = 1;
            boolean hasInv = true;
            for (int j = 2; j * j <= v; j++) {
                if (v % j != 0) {
                    continue;
                }
                int factor = 0;
                while (v % j == 0) {
                    v /= j;
                    factor++;
                }
                factor %= k;
                for (int t = 0; t < factor; t++) {
                    built *= j;
                }
                for (int t = 0; t < (k - factor) % k; t++) {
                    inv *= j;
                    if (inv > limit) {
                        hasInv = false;
                    }
                }
            }
            built *= v;
            for(int j = 1; j < k; j++){
                inv *= v;
                if (inv > limit) {
                    hasInv = false;
                }
            }

            if (hasInv) {
                invMap.put(built, (int) inv);
            }
            a[i] = built;
            cntMap.put(a[i], cntMap.getOrDefault(a[i], 0) + 1);
        }

        LongList xk = new LongList();
        long limit2 = (long) 1e10;
        for (int i = 1; i <= 100000; i++) {
            long v = 1;
            boolean valid = true;
            for (int j = 0; j < k; j++) {
                v *= i;
                if (v > limit2) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                xk.add(v);
            }
        }

        if (k <= 3) {
            long ans = 0;
            for (Map.Entry<Integer, Integer> entry : cntMap.entrySet()) {
                int key = entry.getKey();
                if (!invMap.containsKey(key)) {
                    continue;
                }
                int inv = invMap.get(key);
                if(inv == key){
                    ans += (long) entry.getValue() * (entry.getValue() - 1);
                }else{
                    ans += (long)entry.getValue() * (cntMap.getOrDefault(inv, 0));
                }

            }
            out.println(ans / 2);
            return;
        }

        long ans = 0;
        for (Map.Entry<Integer, Integer> entry : cntMap.entrySet()) {
            int key = entry.getKey();
            for(int j = 0, until = xk.size(); j < until; j++){
                long v = xk.get(j);
                if(v % key != 0){
                    continue;
                }
                if(v / key > limit){
                    continue;
                }
                int other = (int) (v / key);
                if(other == key){
                    ans += (long)entry.getValue() * (entry.getValue() - 1);
                }else{
                    ans += (long)entry.getValue() * cntMap.getOrDefault(other, 0);
                }
            }
        }

        out.println(ans / 2);
    }
}
