package on2020_01.on2020_01_15_Educational_Codeforces_Round_80__Rated_for_Div__2_.F__Red_Blue_Graph1;



import template.graph.LongDinicBeta;
import template.graph.LongMinCostMaxFlow;
import template.graph.MinCostMaxFlow;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class FRedBlueGraph {
    int n1, n2;

    int idOfLeft(int i) {
        return i;
    }

    int idOfRight(int i) {
        return n1 + i;
    }

    int idOfSrc() {
        return n1 + n2;
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }

    long inf = (long) 1e8;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n1 = in.readInt();
        n2 = in.readInt();
        int m = in.readInt();
        int r = in.readInt();
        int b = in.readInt();

        LongMinCostMaxFlow mcmf = new LongMinCostMaxFlow(idOfDst() + 1, idOfSrc(), idOfDst());
        char[] leftColors = in.readString().toCharArray();
        char[] rightColors = in.readString().toCharArray();

        int colorCnt = n1 + n2;
        for (int i = 0; i < n1; i++) {
            if (leftColors[i] == 'R') {
                mcmf.getChannel(idOfSrc(), idOfLeft(i), -inf).modify(1, 0);
                mcmf.getChannel(idOfSrc(), idOfLeft(i), 0).modify(inf, 0);
            } else if (leftColors[i] == 'B') {
                mcmf.getChannel(idOfLeft(i), idOfDst(), -inf).modify(1, 0);
                mcmf.getChannel(idOfLeft(i), idOfDst(), 0).modify(inf, 0);
            } else {
                mcmf.getChannel(idOfSrc(), idOfLeft(i), 0).modify(inf, 0);
                mcmf.getChannel(idOfLeft(i), idOfDst(), 0).modify(inf, 0);
                colorCnt--;
            }
        }

        LongMinCostMaxFlow.DirectFeeChannel[] reds = new LongMinCostMaxFlow.DirectFeeChannel[m];
        LongMinCostMaxFlow.DirectFeeChannel[] blues = new LongMinCostMaxFlow.DirectFeeChannel[m];

        for (int i = 0; i < n2; i++) {
            if (rightColors[i] == 'B') {
                mcmf.getChannel(idOfSrc(), idOfRight(i), -inf).modify(1, 0);
                mcmf.getChannel(idOfSrc(), idOfRight(i), 0).modify(inf, 0);
            } else if (rightColors[i] == 'R') {
                mcmf.getChannel(idOfRight(i), idOfDst(), -inf).modify(1, 0);
                mcmf.getChannel(idOfRight(i), idOfDst(), 0).modify(inf, 0);
            } else {
                mcmf.getChannel(idOfSrc(), idOfRight(i), 0).modify(inf, 0);
                mcmf.getChannel(idOfRight(i), idOfDst(), 0).modify(inf, 0);
                colorCnt--;
            }
        }

        for (int i = 0; i < m; i++) {
            int left = in.readInt() - 1;
            int right = in.readInt() - 1;
            reds[i] = mcmf.addChannel(idOfLeft(left), idOfRight(right), r).modify(1, 0);
            blues[i] = mcmf.addChannel(idOfRight(right), idOfLeft(left), b).modify(1, 0);
        }


        char[] paint = new char[m];
        Arrays.fill(paint, 'U');
        long cost = 0;
        while (true) {
            long[] state = mcmf.send(1);
            if (state[1] >= 0) {
                break;
            }
            cost -= state[1];
            for (int i = 0; i < m; i++) {
                if (reds[i].getFlow() == 1) {
                    paint[i] = ('R');
                } else if (blues[i].getFlow() == 1) {
                    paint[i] = ('B');
                } else {
                    paint[i] = ('U');
                }
            }
        }


        if (cost <= inf * (colorCnt - 1)) {
            out.println(-1);
            return;
        }

        out.println(-(cost - colorCnt * inf));
        out.println(new String(paint));
    }
}
