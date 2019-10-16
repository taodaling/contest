package contest;

import java.util.Arrays;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        NumberTheory.Modular mod = new NumberTheory.Modular((int) 1e9 + 7);

        int n = in.readInt();
        int[] head = new int[n];
        int[] tail = new int[n];
        for (int i = 0; i < n; i++) {
            head[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            tail[i] = in.readInt();
        }
        int[] limit = new int[n];
        int[] certain = new int[n];
        Arrays.fill(limit, (int) 1e9);
        Arrays.fill(certain, -1);
        int last = 0;
        for (int i = 0; i < n; i++) {
            if (head[i] > last) {
                last = head[i];
                certain[i] = last;
            }
            limit[i] = last;
        }
        last = 0;
        for(int i = n - 1; i >= 0; i--){
            if(tail[i] > last){
                last = tail[i];
                certain[i] = tail[i];
            }
            limit[i] = Math.min(limit[i], last);
        }

        boolean flag = true;
        int l2r = 0;
        for(int i = 0; i < n; i++){
            l2r = Math.max(l2r, certain[i]);
            if(l2r != head[i]){
                flag = false;
            }
        }
        int r2l = 0;
        for(int i = n - 1; i >= 0; i--){
            r2l = Math.max(r2l, certain[i]);
            if(r2l != tail[i]){
                flag = false;
            }
        }

        if(!flag){
            out.println(0);
            return;
        }

        int ans = 1;
        for(int i = 0; i < n; i++){
            if(certain[i] != -1){
                continue;
            }
            ans = mod.mul(ans, limit[i]);
        }

        out.println(ans);
    }
}
