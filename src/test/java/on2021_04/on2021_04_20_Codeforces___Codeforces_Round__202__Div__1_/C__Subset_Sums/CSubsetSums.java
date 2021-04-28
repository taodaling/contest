package on2021_04.on2021_04_20_Codeforces___Codeforces_Round__202__Div__1_.C__Subset_Sums;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class CSubsetSums {
    BitSet[] larger;
    long[] upd;
    long[] val;
    long[] largerSum;
    int[][] S;
    int[][] relation;
    int[] largerIndices;

    int B = 333;

    void update(int s, long x) {
        if (larger[s] != null) {
            upd[s] += x;
        } else {
            for (int i : S[s]) {
                val[i] += x;
            }
            for (int k = 0; k < largerIndices.length; k++) {
                int l = largerIndices[k];
                largerSum[l] += relation[k][s] * x;
            }
        }
    }

    long query(int s) {
        long ans = 0;
        if (larger[s] != null) {
            ans = largerSum[s];
        } else {
            for (int i : S[s]) {
                ans += val[i];
            }
        }
        for (int k = 0; k < largerIndices.length; k++) {
            int l = largerIndices[k];
            ans += relation[k][s] * upd[l];
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        larger = new BitSet[m];
        upd = new long[m];
        val = in.rl(n);
        largerSum = new long[m];
        S = new int[m][];
        IntegerArrayList largerIndicesList = new IntegerArrayList();
        for (int i = 0; i < m; i++) {
            int size = in.ri();
            S[i] = new int[size];
            for (int j = 0; j < size; j++) {
                S[i][j] = in.ri() - 1;
            }
            if (size >= B) {
                larger[i] = new BitSet(n);
                for (int j : S[i]) {
                    larger[i].set(j);
                    largerSum[i] += val[j];
                }
                largerIndicesList.add(i);
            }
        }
        largerIndices = largerIndicesList.toArray();

        relation = new int[largerIndices.length][m];
        for (int l = 0; l < largerIndices.length; l++) {
            int k = largerIndices[l];
            for (int i = 0; i < m; i++) {
                for (int j : S[i]) {
                    if (larger[k].get(j)) {
                        relation[l][i]++;
                    }
                }
            }
        }
        for (int i = 0; i < q; i++) {
            char t = in.rc();
            int s = in.ri() - 1;
            if(t == '?'){
                out.println(query(s));
            }else{
                int x = in.ri();
                update(s, x);
            }
        }
    }
}
