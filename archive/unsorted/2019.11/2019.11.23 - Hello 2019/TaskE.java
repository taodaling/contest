package contest;

import template.algo.IntBinarySearch;
import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] pre = new int[n];
        boolean[] delete = new boolean[n];
        int remain = n;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        MultiWayIntegerDeque deque = new MultiWayIntegerDeque(n + 2, n + 1);
        FindCeilElementInDeque bs = new FindCeilElementInDeque(deque, a);

        List<IntegerList> ans = new ArrayList<>();
        while (remain > 0) {
            int k = findK(remain);
            deque.expandQueueNum(remain + 2);
            deque.clear();
            deque.addLast(0, -1);
            for (int i = 0; i < n; i++) {
                if(delete[i]){
                    continue;
                }
                bs.setElement(a[i]);
                int insert = bs.binarySearch(1, remain + 1);
                pre[i] = deque.peekLast(insert - 1);
                deque.addLast(insert, i);
            }

            int lis = 1;
            while (!deque.isEmpty(lis + 1)) {
                lis++;
            }

            if (lis >= k) {
                IntegerList list = new IntegerList(lis);
                int trace = deque.peekLast(lis);
                while (trace != -1) {
                    list.add(trace);
                    delete[trace] = true;
                    remain--;
                    trace = pre[trace];
                }
                list.reverse();
                ans.add(list);
                continue;
            }
            for (int i = 1; i <= lis; i++) {
                IntegerList list = new IntegerList();
                while (!deque.isEmpty(i)) {
                    list.add(deque.removeFirst(i));
                }
                ans.add(list);
            }
            break;
        }

        out.println(ans.size());
        for (IntegerList list : ans) {
            out.append(list.size()).append(' ');
            for (IntegerIterator iterator = list.iterator(); iterator.hasNext(); ) {
                out.append(a[iterator.next()]).append(' ');
            }
            out.println();
        }
    }

    public int findK(int n) {
        int k = 0;
        while (k * (k + 1) / 2 <= n) {
            k++;
        }
        return k;
    }
}

class FindCeilElementInDeque extends IntBinarySearch {
    private MultiWayIntegerDeque deque;
    private int[] a;

    public void setElement(int element) {
        this.element = element;
    }

    private int element;

    FindCeilElementInDeque(MultiWayIntegerDeque deque, int[] a) {
        this.deque = deque;
        this.a = a;
    }

    @Override
    public boolean check(int mid) {
        return deque.isEmpty(mid) || a[deque.peekLast(mid)] > element;
    }
}
