package template.math;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinearProgrammingTest {
    @Test
    public void test() {
        LinearProgramming lp = new LinearProgramming(1, 1, 1e-8);
        lp.setTargetCoefficient(1, 1);
        lp.setConstraintCoefficient(1, 1, 1);
        lp.setConstraintConstant(1, -1);
        lp.solve();
        Assert.assertTrue(lp.isInfeasible());
        //Assert.assertTrue(!lp.isUnbound());
        //Assert.assertEquals(-1, lp.maxSolution(), 1e-8);
    }
}