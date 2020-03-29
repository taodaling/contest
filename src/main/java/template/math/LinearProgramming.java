package template.math;

/**
 * Linear program class.
 * <br>
 * N constraints and M variables.
 * <br>
 * The target is to find an assignment for each variable to make target expression as large as possible.
 * <br>
 * <pre>
 * Maximize t0+t1*x1+...+tm*xm
 * where following constraint satisfied:
 *   c11*x1+...+c1m*xm <= c10
 *   ...
 *   cn1*x1+...+cnm*xm <= cn0
 *   x1, x2, ..., xm >= 0
 * </pre>
 */
public class LinearProgramming {
    private final double prec;
    private final double inf = 1e50;
    double[][] mat;
    int[] basicVariables;
    int[] basicVariable2RowIndex;
    private boolean unbound;
    private boolean infeasible;

    int n;
    int m;

    public LinearProgramming(int n, int m, double prec) {
        this.prec = prec;
        this.n = n;
        this.m = m + n;
        mat = new double[n + 1][this.m + 2];
        basicVariables = new int[this.m + 2];
        basicVariable2RowIndex = new int[this.m + 2];
        for (int i = 1; i <= this.m; i++) {
            if (i <= m) {
                basicVariable2RowIndex[i] = -1;
            } else {
                basicVariable2RowIndex[i] = i - m;
                basicVariables[i - m] = i;
            }
        }
    }

    public void setConstraintConstant(int constraintId, double noMoreThan) {
        mat[constraintId][0] = noMoreThan;
    }

    public void setConstraintCoefficient(int constraintId, int variableId, double c) {
        mat[constraintId][variableId] = -c;
    }

    public void setTargetConstant(double c) {
        mat[0][0] = c;
    }

    public void setTargetCoefficient(int variableId, double c) {
        mat[0][variableId] = c;
    }

    public double maxSolution() {
        return mat[0][0];
    }

    public boolean isInfeasible() {
        return infeasible;
    }

    public boolean isUnbound() {
        return unbound;
    }

    public double getAssignmentValueForVariable(int i) {
        if (basicVariable2RowIndex[i] == -1) {
            return 0;
        } else {
            return mat[basicVariable2RowIndex[i]][0];
        }
    }

    private boolean initSimplex() {
        if (n == 0) {
            return true;
        }
        int minConstantRow = 1;
        for (int i = 2; i <= n; i++) {
            if (mat[i][0] < mat[minConstantRow][0]) {
                minConstantRow = i;
            }
        }
        if (mat[minConstantRow][0] >= 0) {
            return true;
        }
        double[] originalTargetExpression = mat[0];
        m++;
        mat[0] = new double[m + 1];
        mat[0][m] = -1;
        basicVariable2RowIndex[m] = -1;
        for (int i = 1; i <= n; i++) {
            mat[i][m] = 1;
        }
        pivot(m, minConstantRow);
        while (simplex()) ;
        if (mat[0][0] != 0 || unbound) {
            infeasible = true;
            unbound = false;
            return false;
        }
        if (basicVariable2RowIndex[m] != -1) {
            int row = basicVariable2RowIndex[m];
            int firstNegativeVariable = -1;
            for (int i = 1; i <= m && firstNegativeVariable == -1; i++) {
                if (mat[row][i] != 0) {
                    firstNegativeVariable = i;
                }
            }
            pivot(firstNegativeVariable, row);
        }

        //restore
        m--;
        mat[0] = originalTargetExpression;
        for (int i = 1; i <= m; i++) {
            if (basicVariable2RowIndex[i] == -1) {
                continue;
            }
            int row = basicVariable2RowIndex[i];
            double c = mat[0][i];
            for (int j = 0; j <= m; j++) {
                if (j == i) {
                    mat[0][j] = 0;
                    continue;
                }
                mat[0][j] += mat[row][j] * c;
            }
        }
        normalize();
        return true;
    }

    public void solve() {
        if (!initSimplex()) {
            return;
        }
        while (simplex()) ;
    }

    private void normalize() {
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (mat[i][j] >= -prec && mat[i][j] <= prec) {
                    mat[i][j] = 0;
                }
            }
        }
    }

    private void pivot(int variableId, int row) {
        int basicVariableId = basicVariables[row];
        mat[row][basicVariableId] = -1;
        for (int i = 0; i <= m; i++) {
            if (i == variableId) {
                continue;
            }
            mat[row][i] /= -mat[row][variableId];
        }
        mat[row][variableId] = -1;
        basicVariables[row] = variableId;
        basicVariable2RowIndex[basicVariableId] = -1;
        basicVariable2RowIndex[variableId] = row;
        for (int i = 0; i <= n; i++) {
            if (i == row || mat[i][variableId] == 0) {
                continue;
            }
            double c = mat[i][variableId];
            for (int j = 0; j <= m; j++) {
                if (j == variableId) {
                    mat[i][j] = 0;
                    continue;
                }
                mat[i][j] += mat[row][j] * c;
            }
        }
        normalize();
    }

    private boolean simplex() {
        int firstPositiveVariableId = -1;
        for (int i = 1; i <= m && firstPositiveVariableId == -1; i++) {
            if (mat[0][i] > 0) {
                firstPositiveVariableId = i;
            }
        }
        if (firstPositiveVariableId == -1) {
            return false;
        }
        double maxConstraint = inf;
        int maxConstraintRow = -1;
        for (int i = 1; i <= n; i++) {
            if (mat[i][firstPositiveVariableId] >= 0) {
                continue;
            }
            double constraint = mat[i][0] / (-mat[i][firstPositiveVariableId]);
            if (maxConstraint > constraint) {
                maxConstraint = constraint;
                maxConstraintRow = i;
            }
        }
        if (maxConstraintRow == -1) {
            unbound = true;
            return false;
        }
        pivot(firstPositiveVariableId, maxConstraintRow);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Maximize\n  ").append(mat[0][0]);
        for (int i = 1; i <= m; i++) {
            if (mat[0][i] == 0) {
                continue;
            }
            builder.append("+").append(mat[0][i]).append("x").append(i);
        }
        builder.append("\n").append("Constraints\n");
        for (int i = 1; i <= n; i++) {
            builder.append("  ");
            for (int j = 1; j <= m; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                builder.append(-mat[i][j]).append("x").append(j).append("+");
            }
            if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '+') {
                builder.setLength(builder.length() - 1);
            }
            builder.append("<=").append(mat[i][0]).append("\n");
        }

        builder.append("\n").append("Assignment\n");
        for (int i = 1; i <= m; i++) {
            builder.append("  x").append(i).append("=").append(getAssignmentValueForVariable(i)).append("\n");
        }

        return builder.toString();
    }
}
