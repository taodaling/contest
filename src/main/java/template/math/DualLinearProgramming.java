package template.math;

/**
 * <br>
 * N constraints and M variables.
 * <br>
 * The target is to find an assignment for each variable to make target expression as large as possible.
 * <br>
 * <pre>
 * Minimize t0+t1*x1+...+tm*xm
 * where following constraint satisfied:
 *   c11*x1+...+c1m*xm >= c10
 *   ...
 *   cn1*x1+...+cnm*xm >= cn0
 *   x1, x2, ..., xm >= 0
 * </pre>
 */
public class DualLinearProgramming {
    LinearProgramming lp;
    int n;
    int m;

    public DualLinearProgramming(int n, int m, double prec) {
        this.n = n;
        this.m = m;
        lp = new LinearProgramming(m, n, prec);
    }

    public void setConstraintConstant(int constraintId, double noMoreThan) {
        lp.setTargetCoefficient(constraintId, noMoreThan);
    }

    public void setConstraintCoefficient(int constraintId, int variableId, double c) {
        lp.setConstraintCoefficient(variableId, constraintId, c);
    }

    public void setTargetConstant(double c) {
        lp.setTargetConstant(c);
    }

    public void setTargetCoefficient(int variableId, double c) {
        lp.setConstraintConstant(variableId, c);
    }

    public void solve() {
        lp.solve();
    }

    public double minSolution() {
        return lp.maxSolution();
    }

    public boolean isInfeasible() {
        return lp.isUnbound();
    }

    public boolean isUnbound() {
        return lp.isInfeasible();
    }

    public double getAssignmentValueForVariable(int i) {
        if (i + n <= lp.m) {
            return -lp.mat[0][i + n];
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= m; i++) {
            double val = getAssignmentValueForVariable(i);
            builder.append("x").append(i).append("=").append(val).append('\n');
        }
        builder.append("min=").append(minSolution());
        return builder.toString();
    }
}