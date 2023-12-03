package template.math;

import org.junit.Assert;
import org.junit.Test;

public class ExpressionSolverTest {
    @Test
    public void test1() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(1, 1, 1, 0, 1, 0, 1);
        Assert.assertEquals(2, ans);
    }

    @Test
    public void test2() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(-1, -1, 1, 0, 1, 0, 1);
        Assert.assertEquals(0, ans);
    }

    @Test
    public void test3() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(1, -1, 1, 0, 1, 0, 1);
        Assert.assertEquals(1, ans);
    }

    @Test
    public void test4() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(1, 0, 1, 0, 1, 0, 1);
        Assert.assertEquals(2, ans);
    }

    @Test
    public void test5() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(2, 4, 6, 1, 3, 1, 3);
        Assert.assertEquals(1, ans);
    }

    @Test
    public void test6() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(2, 4, 6, 0, 3, 1, 3);
        Assert.assertEquals(1, ans);
    }


    @Test
    public void test7() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(2, 4, 6, 0, 3, 0, 3);
        Assert.assertEquals(2, ans);
    }

    @Test
    public void test8() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(0, 0, 8, 0, 3, 0, 3);
        Assert.assertEquals(0, ans);
    }

    @Test
    public void test9() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(0, 0, 0, 0, 3, 0, 3);
        Assert.assertEquals(16, ans);
    }

    @Test
    public void test10() {
        ExpressionSolver solver = new ExpressionSolver();
        long ans = solver.findWaysToAssignXYSatisfyEquation(1, 3, 1, 1, 9, 1, 900);
        Assert.assertEquals(0, ans);
    }

    @Test
    public void bruteForceTest() {
        ExpressionSolver solver = new ExpressionSolver();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                for (int k = 1; k < 100; k++) {
                    for(int la = 0; la < 10; la++){
                        for(int ra = la; ra < 10; ra++){
                            for(int lb = 0; lb < 10; lb++) {
                                for (int rb = lb; rb < 10; rb++) {
                                    if (find(i, j, k, la, ra, lb, rb) != solver.findWaysToAssignXYSatisfyEquation(i, j, k, la, ra, lb, rb)) {
                                        Assert.fail();
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    public int find(int a, int b, int c, int la, int ra, int lb, int rb) {
        int ans = 0;
        for(int i = la; i <= ra; i++){
            for(int j = lb; j <= rb; j++){
                if(a * i + b * j == c){
                    ans++;
                }
            }
        }
        return ans;
    }
}
