package on2021_03.on2021_03_19_Codeforces___Codeforces_Round__245__Div__1_.C__Guess_the_Tree;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class CGuessTheTree {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        if (n == 1) {
            out.println("YES");
            return;
        }
        int[] nonOne = Arrays.stream(a).filter(x -> x != 1).toArray();
        if (nonOne.length * 2 > n) {
            out.println("NO");
            return;
        }
        if (Arrays.stream(a).max().orElse(-1) != n) {
            out.println("NO");
            return;
        }
        Arrays.sort(nonOne);
        int m = nonOne.length;
        boolean[][] tree = new boolean[n + 1][1 << m];
        boolean[][] forest = new boolean[n + 1][1 << m];

        tree[1][0] = true;
        for(int i = 1; i <= n; i++){
            forest[i][0] = true;
        }

        for (int i = 1; i < 1 << m; i++) {
            int bc = Integer.bitCount(i);
            int h = Log2.floorLog(i);
            for (int j = bc; j <= n; j++) {
//                if ((1 << h) == i) {
//                    if (j == nonOne[h]) {
//                        tree[j][i] = forest[j][i] = true;
//                    }
//                    continue;
//                }
                if(j == nonOne[h]) {
                    for (int k = 1; k < j && !tree[j][i]; k++) {
                        int withoutRoot = i - (1 << h);
                        int subset = withoutRoot + 1;
                        while (subset > 0) {
                            subset = (subset - 1) & withoutRoot;
                            if (tree[k][subset] && forest[j - 1 - k][withoutRoot - subset]) {
                                tree[j][i] = true;
                                break;
                            }
                        }
                    }
                }

                forest[j][i] = tree[j][i];
                for(int k = 1; k < j && !forest[j][i]; k++) {
                    int withoutRoot = i;
                    int subset = withoutRoot + 1;
                    while (subset > 0) {
                        subset = (subset - 1) & withoutRoot;
                        if (tree[k][subset] && forest[j - k][withoutRoot - subset]) {
                            tree[j][i] = true;
                            break;
                        }
                    }
                }
            }
        }

        debug.debug("tree", tree);
        debug.debug("forest", forest);
        if(tree[n][(1 << m) - 1]){
            out.println("YES");
        }else{
            out.println("NO");
        }
    }
}
