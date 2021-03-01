package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.problem.BipartiteHamiltonCirclePartition;

import java.util.List;

public class CFoxAndDinner {
    EulerSieve sieve = new EulerSieve((int) 1e5);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList leftIndexList = new IntegerArrayList();
        IntegerArrayList rightIndexList = new IntegerArrayList();
        for (int i = 0; i < n; i++) {
            if (a[i] % 2 == 0) {
                leftIndexList.add(i);
            } else {
                rightIndexList.add(i);
            }
        }
        BipartiteHamiltonCirclePartition partition = new BipartiteHamiltonCirclePartition(leftIndexList.size(), rightIndexList.size());
        for (int i = 0; i < leftIndexList.size(); i++) {
            int leftIndex = leftIndexList.get(i);
            for (int j = 0; j < rightIndexList.size(); j++) {
                int rightIndex = rightIndexList.get(j);
                if (sieve.isPrime(a[leftIndex] + a[rightIndex])) {
                    partition.addEdge(i, j);
                }
            }
        }
        if (!partition.solve()) {
            out.println("Impossible");
            return;
        }
        List<IntegerArrayList> circles = partition.getAllCircle();
        out.println(circles.size());
        for (IntegerArrayList circle : circles) {
            out.append(circle.size()).append(' ');
            for (int x : circle.toArray()) {
                if (x < leftIndexList.size()) {
                    out.append(leftIndexList.get(x) + 1);
                } else {
                    out.append(rightIndexList.get(x - leftIndexList.size()) + 1);
                }
                out.append(' ');
            }
            out.println();
        }
    }
}
