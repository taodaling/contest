package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DUniquePath {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        int n = in.readInt();
        long m = in.readLong();
        int q = in.readInt();

        DSU dsu = new DSU(n);
        List<int[]> type0 = new ArrayList<>(q);
        List<int[]> type1 = new ArrayList<>(q);
        for (int i = 0; i < q; i++) {
            int[] ab = new int[]{in.readInt(), in.readInt()};
            if (in.readInt() == 0) {
                type0.add(ab);
            } else {
                type1.add(ab);
            }
        }

        for (int[] ab : type0) {
            dsu.merge(ab[0], ab[1]);
        }

        for (int[] ab : type1) {
            if (dsu.find(ab[0]) == dsu.find(ab[1])) {
                no();
                return;
            }
        }

        int setCnt = 0;
        for(int i = 0; i < n; i++){
            if(dsu.find(i) == i){
                setCnt++;
            }
        }

        int edge = n - setCnt;
        if(setCnt <= 2 && !type1.isEmpty()){
            no();
            return;
        }

        long max = 0;
        long min = 0;

        if(!type1.isEmpty()) {
            boolean[] circle = new boolean[n];
            for (int[] ab : type1) {
                circle[dsu.find(ab[0])] = true;
                circle[dsu.find(ab[1])] = true;
            }

            int circleCnt = 0;
            for (boolean b : circle){
                if(b){
                    circleCnt++;
                }
            }
            circleCnt = Math.max(circleCnt, 3);
            min = circleCnt;
        }

        max = choose2(setCnt);

        if(edge + min <= m && m <= edge + max){
            yes();
            return;
        }
        no();
    }

    public long choose2(long n){
        return n * (n - 1) / 2;
    }

    FastOutput out;

    public void no() {
        out.println("No");
    }

    public void yes() {
        out.println("Yes");
    }
}

