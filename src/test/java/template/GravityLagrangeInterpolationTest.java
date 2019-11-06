package template;

import org.junit.Assert;
import org.junit.Test;

public class GravityLagrangeInterpolationTest {
    public boolean near(double a, double b){
        return Math.abs(a - b) < 1e-8;
    }

    @Test
    public void test() {
        GravityLagrangeInterpolation gli = new GravityLagrangeInterpolation(0);
        gli.addPoint(0, 0);
        gli.addPoint(-1, 1);
        gli.addPoint(1, 1);
        GravityLagrangeInterpolation.Polynomial p = gli.preparePolynomial();
        Assert.assertTrue(near(p.getCoefficient(0), 0));
        Assert.assertTrue(near(p.getCoefficient(1), 0));
        Assert.assertTrue(near(p.getCoefficient(2), 1));

        Assert.assertTrue(near(p.function(2), 4));
        Assert.assertTrue(near(p.function(-2), 4));

        Assert.assertTrue(near(gli.getYByInterpolation(2), 4));
        Assert.assertTrue(near(gli.getYByInterpolation(-2), 4));
    }

    @Test
    public void test2() {
        GravityLagrangeInterpolation gli = new GravityLagrangeInterpolation(0);
        gli.addPoint(0, 1);
        gli.addPoint(1, 1);
        gli.addPoint(-1, 3);
        GravityLagrangeInterpolation.Polynomial p = gli.preparePolynomial();
        Assert.assertTrue(near(p.getCoefficient(0), 1));
        Assert.assertTrue(near(p.getCoefficient(1), -1));
        Assert.assertTrue(near(p.getCoefficient(2), 1));

        Assert.assertTrue(near(p.function(2), 3));
        Assert.assertTrue(near(p.function(-2), 7));

        Assert.assertTrue(near(gli.getYByInterpolation(2), 3));
        Assert.assertTrue(near(gli.getYByInterpolation(-2), 7));
    }
}
