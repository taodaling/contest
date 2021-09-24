package on2021_09.on2021_09_20_Codeforces___Codeforces_Round__743__Div__1_.B__Xor_of_3;



import template.datastructure.BitPreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class BXorOf3 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        a = in.ri(n);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum ^= a[i];
        }
        if (sum == 1) {
            out.println("NO");
            return;
        }
        ops.clear();
        if (n % 2 == 1) {
            solve(a, 0, n - 1);
        } else {
            BitPreSum ps = new BitPreSum(n);
            ps.init(n, i -> a[i] == 1);
            boolean find = false;
            for (int i = 0; i < n; i += 2) {
                if (ps.prefix(i) % 2 == 0) {
                    solve(a, 0, i);
                    solve(a, i + 1, n - 1);
                    find = true;
                    break;
                }
            }
            if (!find) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");
        out.println(ops.size());
        for (int op : ops.toArray()) {
            out.append(op + 1).append(' ');
        }
        for(int i = 0; i < n; i++){
            assert a[i] == 0;
        }

        out.println();
    }

    int[] a;
    IntegerArrayList ops = new IntegerArrayList();

    public void solve(int[] a, int l, int r) {
        if(l > r){
            return;
        }
        assert (r - l + 1) % 2 == 1;
        for (int i = l; i + 2 <= r; i += 2) {
            op(i);
        }
        assert a[r] == 0;
        for(int i = r - 2; i >= l; i -= 2){
            op(i);
        }
    }

    public void op(int i) {
        assert i >= 0 && i + 2 < a.length;
        int xor = a[i] ^ a[i + 1] ^ a[i + 2];
        a[i] = a[i + 1] = a[i + 2] = xor;
        ops.add(i);
    }
}
