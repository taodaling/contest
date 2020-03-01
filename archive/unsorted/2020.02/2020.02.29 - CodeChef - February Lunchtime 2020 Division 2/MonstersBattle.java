package contest;

import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MonstersBattle {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Comparator<Integer> rev = (a, b) -> -a.compareTo(b);
        PriorityQueue<Integer>[] ap = new PriorityQueue[]{new PriorityQueue(n), new PriorityQueue(n, rev)};
        PriorityQueue<Integer>[] bp = new PriorityQueue[]{new PriorityQueue(m), new PriorityQueue(m, rev)};
        int[] va = new int[n];
        int[] vb = new int[m];
        char[] typeA = new char[n];
        char[] typeB = new char[m];
        for(int i = 0; i < n; i++){
            va[i] = in.readInt();
        }
        in.readString(typeA, 0);
        for(int i = 0; i < m; i++){
            vb[i] = in.readInt();
        }
        in.readString(typeB, 0);
        long sumA = 0;
        long sumB = 0;
        for (int i = 0; i < n; i++) {
            int v = va[i];
            if (typeA[i] == 'A') {
                ap[0].add(v);
            } else {
                ap[1].add(v);
            }
            sumA += v;
        }
        for (int i = 0; i < m; i++) {
            int v = vb[i];
            if (typeB[i] == 'A') {
                bp[0].add(v);
            } else {
                bp[1].add(v);
            }
            sumB += v;
        }

        long ans = maxDiff(ap, bp, sumA, sumB);
        out.println(ans);
    }

    public static long maxDiff(PriorityQueue<Integer>[] a, PriorityQueue<Integer>[] b,
                               long sumA, long sumB) {
        long cur = sumA - sumB;
        if(!b[1].isEmpty() && !a[0].isEmpty()){
            a[1].add(a[0].remove());
            sumB -= b[1].remove();
            cur = Math.max(cur, -maxDiff(b, a, sumB, sumA));
        }
        return cur;
    }
}
