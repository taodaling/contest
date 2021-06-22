package on2021_06.on2021_06_19_Library_Checker.Characteristic_Polynomial;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.Power;
import template.polynomial.ModGravityLagrangeInterpolation;

public class CharacteristicPolynomial {
    int mod = 998244353;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if(n == 0){
            out.println(1);
            return;
        }
        ModMatrix mat = new ModMatrix(n, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                mat.set(i, j, in.ri());
            }
        }
        ModGravityLagrangeInterpolation.Polynomial p = mat.getCharacteristicPolynomial(new Power(mod));
        int[] ans = p.toArray();
        for(int i = 0; i <= n; i++){
            out.append(ans.length <= i ? 0 : ans[i]).append(' ');
        }
    }
}
