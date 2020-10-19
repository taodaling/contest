package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.math.PermutationUtils;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class DChainReaction {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int[] perm = IntStream.range(0, 4).toArray();
        int[][] pts = new int[4][2];
        int[][] moved = new int[4][];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.readInt();
            }
        }

        minCost = inf;
        do {
            for (int i = 0; i < 4; i++) {
                moved[i] = pts[perm[i]];
            }
            solve(moved, perm);
        } while (PermutationUtils.nextPermutation(perm));

        if (minCost == inf) {
            out.println(-1);
            return;
        }
        out.println(minCost);
        for (int[] pt : targetPoint) {
            out.print(pt[0]);
            out.append(' ');
            out.println(pt[1]);
        }
    }

    int[][] targetPoint = new int[4][2];
    int minCost;
    int inf = (int) 2e9;
    int[] invPerm = new int[4];

    public void solve(int[][] pts, int[] perm) {
        for (int i = 0; i < 4; i++) {
            invPerm[perm[i]] = i;
        }

        int[] lt = pts[0];
        int[] ld = pts[1];
        int[] rd = pts[2];
        int[] rt = pts[3];
        int[] top = new int[]{
                lt[1], rt[1], DigitUtils.floorAverage(lt[1], rt[1])
        };
        int[] left = new int[]{
                lt[0], ld[0], DigitUtils.floorAverage(lt[0], ld[0])
        };
        int[] down = new int[]{
                ld[1], rd[1], DigitUtils.floorAverage(ld[1], rd[1])
        };
        int[] right = new int[]{
                rd[0], rt[0], DigitUtils.floorAverage(rd[0], rt[0])
        };
        for (int t : top) {
            for (int l : left) {
                for (int d : down) {
                    for (int r : right) {
                        if(t <= d || r <= l){
                            continue;
                        }

                        Arrays.fill(movedTime, 0);
                        Arrays.fill(maxMovement, 0);
                        move(l, lt[0], 0);
                        move(t, lt[1], 0);
                        move(l, ld[0], 1);
                        move(d, ld[1], 1);
                        move(r, rd[0], 2);
                        move(d, rd[1], 2);
                        move(r, rt[0], 3);
                        move(t, rt[1], 3);

                        int maxTime = Arrays.stream(movedTime).max().orElse(-1);
                        if (maxTime > 1) {
                            continue;
                        }
                        int max = Arrays.stream(maxMovement).max().orElse(-1);
                        if (max < minCost) {
                            minCost = max;
                            targetPoint[invPerm[0]][0] = l;
                            targetPoint[invPerm[0]][1] = t;

                            targetPoint[invPerm[1]][0] = l;
                            targetPoint[invPerm[1]][1] = d;

                            targetPoint[invPerm[2]][0] = r;
                            targetPoint[invPerm[2]][1] = d;

                            targetPoint[invPerm[3]][0] = r;
                            targetPoint[invPerm[3]][1] = t;
                        }
                    }
                }
            }
        }


    }


    int[] movedTime = new int[4];
    int[] maxMovement = new int[4];

    public void move(int a, int b, int index) {
        if (a == b) {
            return;
        }
        movedTime[index]++;
        maxMovement[index] = Math.max(maxMovement[index], Math.abs(a - b));
    }
}
