package contest;

import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        long[] b = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readChar() - '0';
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.readChar() - '0';
        }
        long[] req = new long[n - 1];
        req[0] = b[0] - a[0];
        for (int i = 1; i < n - 1; i++) {
            req[i] = b[i] - (req[i - 1] + a[i]);
        }
        if (req[n - 2] + a[n - 1] != b[n - 1]) {
            out.println(-1);
            return;
        }

        long ans = 0;
        for(int i = 0; i < n - 1; i++){
            ans += Math.abs(req[i]);
        }


        List<int[]> op = new ArrayList<>(100000);
        boolean[] inque = new boolean[n - 1];
        Deque<Integer> deque = new ArrayDeque<>(n - 1);
        for(int i = 0; i < n - 1; i++){
            deque.addLast(i);
            inque[i] = true;
        }

        while(op.size() < 100000 && !deque.isEmpty()){
            int head = deque.removeFirst();
            inque[head] = false;

            if(req[head] > 0){
                if(a[head] < 9 && a[head + 1] < 9){
                    req[head]--;
                    a[head]++;
                    a[head + 1]++;
                    op.add(SequenceUtils.wrapArray(head, 1));
                    add(deque, inque, head);
                    add(deque, inque, head - 1);
                    add(deque, inque, head + 1);
                }
            }else if(req[head] < 0){
                if(a[head + 1] > 0 && (head == 0 && a[head] > 1 || head > 0 && a[head] > 0)){
                    req[head]++;
                    a[head]--;
                    a[head + 1]--;
                    op.add(SequenceUtils.wrapArray(head, -1));
                    add(deque, inque, head);
                    add(deque, inque, head - 1);
                    add(deque, inque, head + 1);
                }
            }
        }

        out.println(ans);
        for(int[] x: op){
            out.append(x[0] + 1).append(' ').append(x[1]).println();
        }
    }

    public void add(Deque<Integer> deque, boolean[] inque, int x){
        if(x < 0 || x >= inque.length || inque[x]){
            return;
        }
        deque.addLast(x);
        inque[x] = true;
    }
}
