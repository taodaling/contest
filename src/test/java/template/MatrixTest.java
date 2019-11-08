package template;

import org.junit.Assert;
import org.junit.Test;


public class MatrixTest {
    private double delta = 1e-8;

    @Test
    public void test1() {
        Matrix m = new Matrix(new double[][] {{2, 1}, {1, 2}});
        GravityLagrangeInterpolation.Polynomial p = m.getCharacteristicPolynomial();
        Assert.assertEquals(2, p.getRank());
        Assert.assertEquals(3, p.getCoefficient(0), delta);
        Assert.assertEquals(-4, p.getCoefficient(1), delta);
        Assert.assertEquals(1, p.getCoefficient(2), delta);
    }

    @Test
    public void test2() {
        Matrix m = new Matrix(new double[][] {{1, 1, 0}, {1, 1, 1}, {0, 1, 1}});
        GravityLagrangeInterpolation.Polynomial p = m.getCharacteristicPolynomial();
        Assert.assertEquals(3, p.getRank());
        Assert.assertEquals(1, p.getCoefficient(0), delta);
        Assert.assertEquals(1, p.getCoefficient(1), delta);
        Assert.assertEquals(-3, p.getCoefficient(2), delta);
        Assert.assertEquals(1, p.getCoefficient(3), delta);
    }
}