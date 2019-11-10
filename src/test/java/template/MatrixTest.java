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

    @Test
    public void test4() {
        Matrix matrix = new Matrix(new double[][]{
                {0,0,2},
                {1,2,1},
                {2,2,0}
        });

        GravityLagrangeInterpolation.Polynomial p = matrix.getCharacteristicPolynomial();
        Matrix mat = new Matrix(3, 3);
        mat.asStandard();
        Matrix ans = new Matrix(3, 3);
        for(int i = 0; i <= p.getRank(); i++){
            double c = p.getCoefficient(i);
            ans = Matrix.plus(ans, Matrix.mul(mat, c));
            mat = Matrix.mul(mat, matrix);
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Assert.assertEquals(0, ans.get(i, j), 1e-8);
            }
        }
    }
}