package on2021_09.on2021_09_26_AtCoder___AtCoder_Regular_Contest_127.C___Binary_Strings;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class CBinaryStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int N = in.ri();
        char[] s = new char[N + 1];
        int n = in.rs(s);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        SequenceUtils.reverse(s, 0, n - 1);
        Binary binary = new Binary(s);
        IntegerArrayList list = new IntegerArrayList(N + 10);
        list.add(1);
        binary.subOne();
        while (binary.highest >= 0) {
            int k = N + 1 - (list.size() + 1);
            if (binary.highest == k) {
                list.add(1);
                binary.delHighest();
            }else{
                list.add(0);
                binary.subOne();
            }
            debug.debug("list.size", list.size());
            debug.debug("binary", binary);
        }

        for(int x : list.toArray()){
            out.append(x);
        }
    }

    Debug debug = new Debug(false);

}

class Binary {
    char[] s;
    int highest;

    public Binary(char[] s) {
        this.s = s;
        this.highest = s.length - 1;
        recalcHighest();
    }

    private void recalcHighest() {
        while (highest >= 0 && s[highest] == 0) {
            highest--;
        }
    }

    public void subOne() {
        int cur = 0;
        while (s[cur] == 0) {
            s[cur] = 1;
            cur++;
        }
        s[cur] = 0;
        recalcHighest();
    }

    public void delHighest() {
        s[highest] = 0;
        recalcHighest();
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for(int i = highest; i >= 0; i--){
            ans.append((int)s[i]);
        }
        return ans.toString();
    }
}
