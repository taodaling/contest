package template;

import org.junit.Assert;
import org.junit.Test;

public class ModMatrixTest {
    NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
    NumberTheory.Power pow = new NumberTheory.Power(mod);

    @Test
    public void test1() {
        ModMatrix m = new ModMatrix(new int[][] {{2, 1}, {1, 2}});
        GravityModLagrangeInterpolation.Polynomial p = m.getCharacteristicPolynomial(pow);
        Assert.assertEquals(2, p.getRank());
        Assert.assertEquals(3, p.getCoefficient(0));
        Assert.assertEquals(mod.valueOf(-4), p.getCoefficient(1));
        Assert.assertEquals(1, p.getCoefficient(2));
    }

    @Test
    public void test2() {
        ModMatrix m = new ModMatrix(new int[][] {{1, 1, 0}, {1, 1, 1}, {0, 1, 1}});
        GravityModLagrangeInterpolation.Polynomial p = m.getCharacteristicPolynomial(pow);
        Assert.assertEquals(3, p.getRank());
        Assert.assertEquals(1, p.getCoefficient(0));
        Assert.assertEquals(1, p.getCoefficient(1));
        Assert.assertEquals(mod.valueOf(-3), p.getCoefficient(2));
        Assert.assertEquals(1, p.getCoefficient(3));
    }
}
