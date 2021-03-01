package contest;

import java.io.InputStream;
import java.io.OutputStream;

import net.egork.chelper.tester.Verdict;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import chelper.AbstractInteractor;

public class CInteractor extends AbstractInteractor {
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int k = input.ri();
        while (k-- > 0) {
            int n = input.ri();
            int[] a = input.ri(n);
            solutionInput.println(n).flush();
            int rem = n + Log2.floorLog(n);
            boolean ok = false;
            while (rem >= 0) {
                char c = solutionOutput.rc();
                if (c == '?') {
                    rem--;
                    if(rem < 0){
                        return Verdict.WA;
                    }
                    int l = solutionOutput.ri();
                    int r = solutionOutput.ri();
                    long ans = compute(a, solutionOutput.ri(l), solutionOutput.ri(r));
                    if (ans > n) {
                        return Verdict.WA;
                    }
                    solutionInput.println(ans).flush();
                } else {
                    int t = solutionOutput.ri();
                    for (int i = 0; i < t; i++) {
                        int x = solutionOutput.ri() - 1;
                        if(a[x] != 0){
                            return Verdict.WA;
                        }
                        a[x] = -2;
                    }
                    for (int x : a) {
                        if (x == 0) {
                            return Verdict.WA;
                        }
                    }
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                return Verdict.WA;
            }
        }
        return Verdict.OK;
    }

    int[] count(int[] a, int[] x) {
        int[] ans = new int[3];
        for (int t : x) {
            ans[a[t - 1] + 1]++;
        }
        return ans;
    }

    long compute(int[] a, int[] l, int[] r) {
        int[] cl = count(a, l);
        int[] cr = count(a, r);
        return (long) cl[0] * cr[0] + (long) cl[2] * cr[2] - (long) cl[0] * cr[2] - (long) cl[2] * cr[0];
    }
}
