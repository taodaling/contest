package on2020_02.on2020_02_22_Codeforces_Round__454__Div__1__based_on_Technocup_2018_Elimination_Round_4_.A__Shockers;



import template.binary.Bits;
import template.binary.CachedBitCount;
import template.binary.CachedLog2;
import template.io.FastInput;
import template.io.FastOutput;

public class AShockers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] buf = new char[100000];
        int chars = 'z' - 'a' + 1;
        int mask = (1 << chars) - 1;
        int i;
        for (i = 0; i < n; i++) {
            int c = in.readChar();
            int m = in.readString(buf, 0);
            if (c == '.') {
                for (int j = 0; j < m; j++) {
                    mask = Bits.setBit(mask, buf[j] - 'a', false);
                }
            } else if(c == '!'){
                int set = 0;
                for (int j = 0; j < m; j++) {
                    set = Bits.setBit(set, buf[j] - 'a', true);
                }
                mask &= set;
            }else{
                int impossible = (1 << chars) - 1;
                for (int j = 0; j < m; j++) {
                    impossible = Bits.setBit(impossible, buf[j] - 'a', false);
                }
                mask &= impossible;
            }

            if (Integer.bitCount(mask) == 1) {
                break;
            }
        }

        int ans = 0;
        for (i++; i < n - 1; i++) {
            int c = in.readChar();
            int m = in.readString(buf, 0);
            if (c == '.') {

            } else{
                ans++;
            }
        }

        out.println(ans);
    }
}
