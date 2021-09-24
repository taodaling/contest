package on2021_08.on2021_08_29_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.C__Compressed_Bracket_Sequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class CCompressedBracketSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] c = in.rl(n);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 1) {
                continue;
            }
            long l = 1;
            long r = c[i];
            long local = 0;
            for (int j = i + 1; j < n && r >= 0; j++) {
                if (j % 2 == 0) {
                    l += c[j];
                    r += c[j];
                } else {
                    l -= c[j];
                    r -= c[j];
                    if (l <= 0) {
                        if(r >= 0) {
                            local += -l + 1;
                        }else{
                            local += r - l + 1;
                        }
                        l = 0;
                    }
                }
            }
            debug.debug("i", i);
            debug.debug("local", local);
            ans += local;
        }

        out.println(ans);
    }

    Debug debug = new Debug(false);
}
