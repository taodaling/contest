package template;

import org.junit.Assert;
import org.junit.Test;
import template.math.LinearFeedbackShiftRegister;

public class LinearFeedbackShiftRegisterTest {

    private void assertNear(double a, double b){
        Assert.assertTrue(Math.abs(a - b) < 1e-8);
    }

    @Test
    public void test1() {
        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister();
        LinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator();
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
    }

    @Test
    public void test2() {
        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister();
        lfsr.add(1);
        lfsr.add(2);
        lfsr.add(4);
        Assert.assertEquals(1, lfsr.length());
        LinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(1);
        assertNear(estimator.next(), 2);
        assertNear(estimator.next(), 4);
        assertNear(estimator.next(), 8);
        assertNear(estimator.next(), 16);
    }

    @Test
    public void test3() {
        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister();
        lfsr.add(0);
        lfsr.add(1);
        lfsr.add(1);
        lfsr.add(2);
        lfsr.add(3);
        lfsr.add(5);
        Assert.assertEquals(2, lfsr.length());
        LinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(2, 2);
        assertNear(estimator.next(), 4);
        assertNear(estimator.next(), 6);
        assertNear(estimator.next(), 10);
        assertNear(estimator.next(), 16);
    }

    @Test
    public void test4() {
        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister();
        lfsr.add(1);
        lfsr.add(2);
        lfsr.add(5);
        lfsr.add(11);
        lfsr.add(26);
        lfsr.add(59);
        Assert.assertEquals(2, lfsr.length());
        LinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(1, 1);
        assertNear(estimator.next(), 4);
        assertNear(estimator.next(), 7);
        assertNear(estimator.next(), 19);
        assertNear(estimator.next(), 40);
    }

    @Test
    public void test5() {
        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister();
        lfsr.add(0);
        lfsr.add(0);
        lfsr.add(1);
        lfsr.add(1);
        lfsr.add(1);
        lfsr.add(1);
        Assert.assertEquals(3, lfsr.length());
        LinearFeedbackShiftRegister.Estimator estimator = lfsr.newEstimator(1, 1, 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
        assertNear(estimator.next(), 0);
    }
}
