package on2020_03.on2020_03_05_Dwango_Programming_Contest_V.C___k_DMC;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.IntegerRange2DequeAdapter;

public class CKDMC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        int q = in.readInt();

        for (int i = 0; i < q; i++) {
            int k = in.readInt();
            long ans = 0;
            long dCnt = 0;
            long mCnt = 0;
            long dmCnt = 0;
            IntegerDeque deque = new IntegerRange2DequeAdapter(x -> x, 0, n - 1);
            for (int j = 0; j < n; j++) {
                while (j - deque.peekFirst() >= k) {
                    int index = deque.removeFirst();
                    if (s[index] == 'D') {
                        dmCnt -= mCnt;
                        dCnt--;
                    }
                    if (s[index] == 'M') {
                        mCnt--;
                    }
                }
                if (s[j] == 'D') {
                    dCnt++;
                }
                if (s[j] == 'M') {
                    mCnt++;
                    dmCnt += dCnt;
                }
                if (s[j] == 'C') {
                    ans += dmCnt;
                }
            }
            out.println(ans);
        }
    }
}
