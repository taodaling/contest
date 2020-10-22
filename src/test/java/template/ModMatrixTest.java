package template;

import org.junit.Assert;
import org.junit.Test;
import template.math.ModMatrix;
import template.math.ModSparseMatrix;
import template.math.Modular;
import template.math.Power;
import template.polynomial.ModGravityLagrangeInterpolation;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ModMatrixTest {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);

    @Test
    public void test1() {
        ModMatrix m = new ModMatrix(new int[]{2, 1,
                1, 2}, 2);
        ModGravityLagrangeInterpolation.Polynomial p = m.getCharacteristicPolynomial(pow);
        Assert.assertEquals(2, p.getRank());
        Assert.assertEquals(3, p.getCoefficient(0));
        Assert.assertEquals(mod.valueOf(-4), p.getCoefficient(1));
        Assert.assertEquals(1, p.getCoefficient(2));
    }

    @Test
    public void test2() {
        ModMatrix m = new ModMatrix(new int[]{1, 1, 0,
                1, 1, 1,
                0, 1, 1}, 3);
        ModGravityLagrangeInterpolation.Polynomial p = m.getCharacteristicPolynomial(pow);
        Assert.assertEquals(3, p.getRank());
        Assert.assertEquals(1, p.getCoefficient(0));
        Assert.assertEquals(1, p.getCoefficient(1));
        Assert.assertEquals(mod.valueOf(-3), p.getCoefficient(2));
        Assert.assertEquals(1, p.getCoefficient(3));
    }

    @Test
    public void test3() {
        int n = 100;

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i + 1;
        }
        ModMatrix mat = new ModMatrix(n, n);
        for (int j = 1; j < n; j++) {
            mat.set(j, j - 1, 1);
        }
        for (int i = 0; i < n; i++) {
            mat.set(0, i, a[i]);
        }

        ModGravityLagrangeInterpolation.Polynomial p = mat.getCharacteristicPolynomial(pow);
        Assert.assertEquals(n, p.getRank());
        Assert.assertEquals(1, p.getCoefficient(n));
        for (int i = 1; i <= n; i++) {
            int exp = mod.valueOf(-1);
            exp = mod.mul(exp, a[i - 1]);
            Assert.assertEquals(exp, p.getCoefficient(n - i));
        }
    }

    @Test
    public void test4() {
        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);

        ModMatrix matrix = new ModMatrix(new int[]{
                0, 0, 2,
                1, 2, 1,
                2, 2, 0
        }, 3);

        ModGravityLagrangeInterpolation.Polynomial p = matrix.getCharacteristicPolynomial(pow);
        ModSparseMatrix x = new ModSparseMatrix(matrix);
        IntegerArrayList list = x.getMinimalPolynomialByRandom(mod.getMod());


        ModMatrix mat = new ModMatrix(3, 3);
        mat.asStandard(mod.getMod());
        ModMatrix ans = new ModMatrix(3, 3);
        for (int i = 0; i <= p.getRank(); i++) {
            int c = p.getCoefficient(i);
            ans = ModMatrix.plus(ans, ModMatrix.mul(mat, c, mod.getMod()), mod.getMod());
            mat = ModMatrix.mul(mat, matrix, mod.getMod());
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Assert.assertEquals(0, ans.get(i, j));
            }
        }
    }

}
