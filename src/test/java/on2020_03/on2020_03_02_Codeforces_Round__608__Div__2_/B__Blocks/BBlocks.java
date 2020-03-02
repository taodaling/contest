package on2020_03.on2020_03_02_Codeforces_Round__608__Div__2_.B__Blocks;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class BBlocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] s = new int[n];
        int[] cnt = new int[2];
        for (int i = 0; i < n; i++) {
            s[i] = in.readChar() == 'B' ? 0 : 1;
            cnt[s[i]]++;
        }
        IntegerList ans;
        if (cnt[0] % 2 == n % 2) {
            for (int i = 0; i < n; i++) {
                s[i] = 1 - s[i];
            }
            ans = solve(s);
        } else if (cnt[1] % 2 == n % 2) {
            ans = solve(s);
        } else {
            out.println(-1);
            return;
        }

        out.println(ans.size());
        for(int i = 0, end = ans.size(); i < end; i++){
            out.append(ans.get(i) + 1).append(' ');
        }
    }

    public IntegerList solve(int[] data) {
        IntegerList ans = new IntegerList();
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] == 0) {
                ans.add(i);
                data[i] ^= 1;
                data[i + 1] ^= 1;
            }
        }
        return ans;
    }
}
