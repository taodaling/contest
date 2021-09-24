package on2021_08.on2021_08_24_Codeforces___Codeforces_Round__740__Div__1__based_on_VK_Cup_2021___Final__Engine__.C__Bottom_Tier_Reversals;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class CBottomTierReversals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        data = in.ri(n);
        indices = new int[n];
        sol = new IntegerArrayList(5 * n);
        for (int i = 0; i < n; i++) {
            data[i]--;
            if (data[i] % 2 != i % 2) {
                out.println(-1);
                return;
            }
            indices[data[i]] = i;
        }
        for (int i = n - 1; i >= 1; i -= 2) {
            int a = i;
            int b = i - 1;
            add(indices[a] + 1);
            add(indices[b]);
            add(indices[b] + 2);
            add(indices[a] + 1);
            add(i + 1);
            assert indices[a] == a;
            assert indices[b] == b;
        }
        for(int i = 0; i < n; i++){
            assert data[i] == i;
        }
        out.println(sol.size());
        for(int x : sol.toArray()){
            out.append(x).append(' ');
        }
        out.println();
    }

    int[] data;
    int[] indices;

    public void swap(int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
        indices[data[i]] = i;
        indices[data[j]] = j;
    }

    IntegerArrayList sol;

    public void add(int k) {
        assert k % 2 == 1;
        int l = 0;
        int r = k - 1;
        while (l < r) {
            swap(l, r);
            l++;
            r--;
        }
        sol.add(k);
    }
}
