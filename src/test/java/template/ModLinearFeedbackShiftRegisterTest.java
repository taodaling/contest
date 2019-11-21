package template;

import org.junit.Assert;
import org.junit.Test;
import template.math.ModLinearFeedbackShiftRegister;
import template.math.Modular;

public class ModLinearFeedbackShiftRegisterTest {
    Modular mod = new Modular((int)1e9 + 7);

    private void assertNear(double a, double b){
        Assert.assertTrue(Math.abs(a - b) < 1e-8);
    }
    
    @Test
    public void test1() {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod);
        ModLinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator();
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
    }

    @Test
    public void test2() {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod);
        lfsr.add(1);
        lfsr.add(2);
        lfsr.add(4);
        Assert.assertEquals(1, lfsr.length());
        ModLinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(1);
        assertNear(estimator.next(), 2);
        assertNear(estimator.next(), 4);
        assertNear(estimator.next(), 8);
        assertNear(estimator.next(), 16);
    }

    @Test
    public void test3() {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod);
        lfsr.add(0);
        lfsr.add(1);
        lfsr.add(1);
        lfsr.add(2);
        lfsr.add(3);
        lfsr.add(5);
        Assert.assertEquals(2, lfsr.length());
        ModLinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(2, 2);
        assertNear(estimator.next(), 4);
        assertNear(estimator.next(), 6);
        assertNear(estimator.next(), 10);
        assertNear(estimator.next(), 16);
    }

    @Test
    public void test4() {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod);
        lfsr.add(1);
        lfsr.add(2);
        lfsr.add(5);
        lfsr.add(11);
        lfsr.add(26);
        lfsr.add(59);
        Assert.assertEquals(2, lfsr.length());
        ModLinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(1, 1);
        assertNear(estimator.next(), 4);
        assertNear(estimator.next(), 7);
        assertNear(estimator.next(), 19);
        assertNear(estimator.next(), 40);
    }

    @Test
    public void test5() {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod);
        lfsr.add(0);
        lfsr.add(0);
        lfsr.add(1);
        lfsr.add(1);
        lfsr.add(1);
        lfsr.add(1);
        Assert.assertEquals(3, lfsr.length());
        ModLinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(1, 1, 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
    }
}