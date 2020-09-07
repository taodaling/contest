package on2020_09.on2020_09_06_2020_TopCoder_Open_Algo.BigJump;



import template.primitve.generated.graph.*;

import java.util.List;

public class BigJump {
    int N;

    int idOfL(int i) {
        return i;
    }

    int idOfR(int i) {
        return N + i;
    }

    int idOfSrc() {
        return N * 2;
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }

    public int[] construct(int N) {
        this.N = N;
        List<IntegerCostFlowEdge>[] g = IntegerFlow.createCostFlow(idOfDst() + 1);
        for (int i = 0; i < N; i++) {
            IntegerFlow.addCostEdge(g, idOfR(i), idOfL(i), 1, -1);
            if(2 * i % N != i) {
                IntegerFlow.addCostEdge(g, idOfL(i), idOfR(2 * i % N), 1, 0);
            }
            if((2 * i - 1 + N) % N != i) {
                IntegerFlow.addCostEdge(g, idOfL(i), idOfR((2 * i - 1 + N) % N), 1, 0);
            }
        }
        IntegerFlow.addCostEdge(g, idOfSrc(), idOfL(0), 1, 0);
        IntegerFlow.addCostEdge(g, idOfL(0), idOfDst(), 1, 0);
        int[] ans = new IntegerSpfaMinimumCostFlow(g.length).apply(g, idOfSrc(), idOfDst(), 1);
        if (ans[1] != -N) {
            return new int[0];
        }
        int[] res = new int[N];
        res[0] = 0;
        for (int i = 1; i < N; i++) {
            int last = res[i - 1];
            for (IntegerCostFlowEdge e : g[idOfL(last)]) {
                if (e.real && e.flow == 1 && e.to >= idOfR(0) && e.to <= idOfR(N - 1)) {
                    res[i] = e.to - idOfR(0);
                    break;
                }
            }
        }

        return res;
    }
}
