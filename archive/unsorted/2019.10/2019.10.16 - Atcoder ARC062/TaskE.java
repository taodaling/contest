package contest;

import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.LongDiscreteMap;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        Rect[] rects = new Rect[n];
        List<Long> summary = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rects[i] = new Rect();
            for (int j = 0; j < 4; j++) {
                rects[i].colors[j] = in.readInt();
            }
            long s = summaryOf(rects[i]);
            for (int j = 0; j < 4; j++) {
                summary.add(s);
                s = rotate(s);
            }
        }

        LongDiscreteMap dm =
                        new LongDiscreteMap(summary.stream().mapToLong(Long::longValue).toArray(), 0, summary.size());

        int[][] rotations = new int[dm.maxRank() + 1][4];
        for (int i = 0; i < rotations.length; i++) {
            long val = dm.iThElement(i);
            for (int j = 0; j < 4; j++) {
                rotations[i][j] = dm.rankOf(val);
                val = rotate(val);
            }
        }

        int[] cnts = new int[dm.maxRank() + 1];
        for (Rect rect : rects) {
            rect.summary = dm.rankOf(summaryOf(rect));
            for (int s : rotations[rect.summary]) {
                cnts[s] += 1;
            }
        }

        int[] cols = new int[8];
        int[] faces = new int[4];

        long cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int s : rotations[rects[i].summary]) {
                cnts[s] -= 1;
            }
            cols[3] = rects[i].get(0, 0);
            cols[1] = rects[i].get(1, 0);
            cols[0] = rects[i].get(2, 0);
            cols[2] = rects[i].get(3, 0);

            for (int j = i + 1; j < n; j++) {

                for (int s : rotations[rects[j].summary]) {
                    cnts[s] -= 1;
                }

                for (int rj = 0; rj < 4; rj++) {

                    cols[5] = rects[j].get(0, rj);
                    cols[7] = rects[j].get(1, rj);
                    cols[6] = rects[j].get(2, rj);
                    cols[4] = rects[j].get(3, rj);


                    faces[0] = dm.rankOf(summaryOf(cols[3], cols[7], cols[5], cols[1]));
                    faces[1] = dm.rankOf(summaryOf(cols[1], cols[5], cols[4], cols[0]));
                    faces[2] = dm.rankOf(summaryOf(cols[6], cols[2], cols[0], cols[4]));
                    faces[3] = dm.rankOf(summaryOf(cols[7], cols[3], cols[2], cols[6]));

                    if (faces[0] >= 0 && faces[1] >= 0 && faces[2] >= 0 && faces[3] >= 0) {
                        long localCnt = 1;
                        for (int t = 0; t < 4; t++) {
                            localCnt *= cnts[faces[t]];
                            for (int r : rotations[faces[t]]) {
                                cnts[r]--;
                            }
                        }
                        cnt += localCnt;
                        for (int t = 0; t < 4; t++) {
                            for (int r : rotations[faces[t]]) {
                                cnts[r]++;
                            }
                        }
                    }

                }


                for (int s : rotations[rects[j].summary]) {
                    cnts[s] += 1;
                }
            }
        }



        out.println(cnt);
    }

    public long summaryOf(Rect rect) {
        return summaryOf(rect.colors[0], rect.colors[1], rect.colors[2], rect.colors[3]);
    }

    public long summaryOf(int a, int b, int c, int d) {
        long ans = a;
        ans = ans * 1000 + b;
        ans = ans * 1000 + c;
        ans = ans * 1000 + d;
        return ans;
    }

    public long rotate(long x) {
        return (x % 1000) * 1_000_000_000 + x / 1000;
    }

}


class Rect {
    int[] colors = new int[4];

    public int get(int i, int r) {
        return colors[(i + r) & 3];
    }

    int summary;
    int cnt;
}
