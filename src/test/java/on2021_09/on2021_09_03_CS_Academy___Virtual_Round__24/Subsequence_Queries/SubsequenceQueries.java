package on2021_09.on2021_09_03_CS_Academy___Virtual_Round__24.Subsequence_Queries;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.Power;

public class SubsequenceQueries {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int q = in.ri();
        int type = 9;
        ModMatrix[] mats = new ModMatrix[type];
        ModMatrix[] invMats = new ModMatrix[type];
        for (int i = 0; i < 9; i++) {
            mats[i] = new ModMatrix(type + 1, type + 1);
            mats[i].asStandard(mod);
            for (int j = 0; j <= type; j++) {
                mats[i].set(j, i, 1);
            }
            invMats[i] = ModMatrix.inverse(mats[i], pow);
        }
        ModMatrix ps = new ModMatrix(type + 1, type + 1);
        ModMatrix invPs = new ModMatrix(type + 1, type + 1);
        ps.asStandard(mod);
        invPs.asStandard(mod);
        ModMatrix[] prefix = new ModMatrix[s.length];
        ModMatrix[] invPrefix = new ModMatrix[s.length];
        for (int i = 0; i < s.length; i++) {
            invPrefix[i] = invPs;
            ps = ModMatrix.mul(ps, mats[s[i] - 'a'], mod);
            prefix[i] = ps;
            invPs = ModMatrix.mul(invMats[s[i] - 'a'], invPs, mod);
        }

//        int[][] next = new int[type][s.length];
//        int[] reg = new int[type];
//        Arrays.fill(reg, s.length);
//        for(int i = s.length - 1; i >= 0; i--){
//            reg[s[i] - 'a'] = i;
//            for(int j = 0; j < type; j++){
//                next[j][i] = reg[j];
//            }
//        }
        for (int i = 0; i < q; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            ModMatrix vec = new ModMatrix(1, type + 1);
            vec.set(0, type, 1);
            ModMatrix vr = ModMatrix.mul(vec, invPrefix[l], mod);
            ModMatrix vrA = ModMatrix.mul(vr, prefix[r], mod);
            long sum = 0;
            for (int j = 0; j < type; j++) {
                sum += vrA.get(0, j);
            }
            sum %= mod;
            out.println(sum);
        }
    }


}
