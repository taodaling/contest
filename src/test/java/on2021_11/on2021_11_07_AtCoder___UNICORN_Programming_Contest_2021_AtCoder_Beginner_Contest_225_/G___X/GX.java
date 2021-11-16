package on2021_11.on2021_11_07_AtCoder___UNICORN_Programming_Contest_2021_AtCoder_Beginner_Contest_225_.G___X;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;

import java.util.List;

public class GX {
    int H;
    int W;

    public int cell(int i, int j){
        return i * W + j;
    }
    public int src(){
        return H * W;
    }
    public int dst(){
        return src() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        H = in.ri();
        W = in.ri();
        int C = in.ri();
        int[][] A = new int[H][W];
        long sum = 0;
        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                A[i][j] = in.ri();
                sum += A[i][j];
            }
        }

        List<LongFlowEdge>[] g = Graph.createGraph(dst() + 1);
        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                LongFlow.addFlowEdge(g, cell(i, j), dst(), A[i][j]);
                if(j > 0 && i - 1 >= 0){
                    LongFlow.addFlowEdge(g, cell(i - 1, j - 1), cell(i, j), C);
                }else{
                    LongFlow.addFlowEdge(g, src(), cell(i, j), C);
                }
                if(j > 0 && i + 1 < H){
                    LongFlow.addFlowEdge(g, cell(i + 1, j - 1), cell(i, j), C);
                }else{
                    LongFlow.addFlowEdge(g, src(), cell(i, j), C);
                }
            }
        }

        long inf = (long)1e18;
        LongMaximumFlow mf = new LongHLPP();
        long flow = mf.apply(g, src(), dst(), inf);
        out.println(sum - flow);
    }
}
