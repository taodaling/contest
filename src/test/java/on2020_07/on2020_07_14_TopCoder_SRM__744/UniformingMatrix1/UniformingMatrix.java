package on2020_07.on2020_07_14_TopCoder_SRM__744.UniformingMatrix1;



import template.math.Modular;
import template.math.XorGuassianElimination;
import template.utils.ArrayIndex;

public class UniformingMatrix {
    public String isPossible(String[] M) {
        int n = M.length;
        int m = M[0].length();
        ArrayIndex ai = new ArrayIndex(n, m);
        XorGuassianElimination ge = new XorGuassianElimination(n * m + 1, n + m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ge.setLeft(ai.indexOf(i, j), i, 1);
                ge.setLeft(ai.indexOf(i, j), n + j, 1);
                ge.setRight(ai.indexOf(i, j), M[i].charAt(j) == '1' ? 0 : 1);
            }
        }
        for (int j = 0; j < n + m; j++) {
            ge.setLeft(n * m, j, 1);
        }
        ge.setRight(n * m, 0);
        if (!ge.solve()) {
            return "impossible";
        }
        int[] cnts = new int[2];
        for (int i = 0; i < n; i++) {
            cnts[0] += ge.getSolutions()[i];
        }
        for (int i = n; i < n + m; i++) {
            cnts[1] += ge.getSolutions()[i];
        }

        return "possible";
    }
}
