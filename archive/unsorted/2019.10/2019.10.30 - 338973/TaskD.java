package contest;

import template.FastInput;
import template.FastOutput;
import template.IntList;
import template.KMAlgo;
import template.NumberTheory;

public class TaskD {
    NumberTheory.EulerSieve es = new NumberTheory.EulerSieve(10000000);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        IntList oneList = new IntList();
        int last = in.readInt();
        oneList.add(last - 1);
        for (int i = 1; i < n; i++) {
            int x = in.readInt();
            if (x - last != 1) {
                oneList.add(last);
                oneList.add(x - 1);
            }
            last = x;
        }
        oneList.add(last);

        oneList.unique();


        IntList oddList = new IntList();
        IntList evenList = new IntList();
        for (int i = 0; i < oneList.size(); i++) {
            int v = oneList.get(i);
            if (v % 2 == 0) {
                evenList.add(v);
            } else {
                oddList.add(v);
            }
        }


        KMAlgo km = new KMAlgo(oddList.size(), evenList.size());
        for (int i = 0; i < oddList.size(); i++) {
            for (int j = 0; j < evenList.size(); j++) {
                int dist = Math.abs(oddList.get(i) - evenList.get(j));
                if (dist > 2 && es.isPrime(dist)) {
                    km.addEdge(i, j);
                }
            }
        }

        int match = 0;
        for(int i = 0; i < oddList.size(); i++){
            match += km.matchLeft(i) ? 1 : 0;
        }

        int ans = match * 1 + (oddList.size() - match) / 2 * 2 +(evenList.size() - match) / 2 * 2 +
                (oddList.size() - match) % 2 * 3;

        out.println(ans);

    }
}
