package on2021_05.on2021_05_23_Luogu.P1919_____A_B_Problem____FFT______;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.BigInt10;
import template.string.CharArrayCharSequenceAdapter;
import template.utils.PrimitiveBuffers;

public class P1919ABProblemFFT {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int L = 1000000 + 1;
        char[] a = new char[L];
        char[] b = new char[L];
        int na = in.rs(a);
        int nb = in.rs(b);
        BigInt10 ba = new BigInt10(new CharArrayCharSequenceAdapter(a, 0, na - 1));
        BigInt10 bb = new BigInt10(new CharArrayCharSequenceAdapter(b, 0, nb - 1));
        BigInt10 res = ba.mul(bb);
        out.println(res);
//
//        ba.release();
//        bb.release();
//        res.release();
//        PrimitiveBuffers.check();
    }
}
