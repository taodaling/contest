package contest;

import template.math.Modular;
import template.math.XorGuassianElimination;
import template.utils.ArrayIndex;

public class UniformingMatrix {
    public String isPossible(String[] M) {
        int n = M.length;
        int m = M[0].length();
        ArrayIndex ai = new ArrayIndex(n, m);
        XorGuassianElimination ge = new XorGuassianElimination(n * m, n + m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ge.setLeft(ai.indexOf(i, j), i, 1);
                ge.setLeft(ai.indexOf(i, j), n + j, 1);
                ge.setRight(ai.indexOf(i, j), M[i].charAt(j) == '1' ? 0 : 1);
            }
        }
        if (!ge.solve()) {
            return "impossible";
        }
        int[] cnts = new int[n];
        for(int i = 0; i < n; i++){
            cnts[0] += ge.getSolutions()[i];
        }
        for(int i = n; i < n + m; i++){
            cnts[1] += ge.getSolutions()[i];
        }

        if(cnts[0] % 2 != cnts[1] % 2 && n % 2 == m % 2){
            return "impossible";
        }

        return "possible";
    }
}
