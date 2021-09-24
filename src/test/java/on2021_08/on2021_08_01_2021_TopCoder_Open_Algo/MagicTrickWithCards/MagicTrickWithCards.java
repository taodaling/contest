package on2021_08.on2021_08_01_2021_TopCoder_Open_Algo.MagicTrickWithCards;



public class MagicTrickWithCards {
    public int[] findTheirCard(String[] queryCards, int[] queryX) {
        int[] ans = new int[queryCards.length];
        for (int i = 0; i < queryCards.length; i++) {
            ans[i] = solve(queryCards[i], queryX[i]);
        }
        return ans;
    }

    public int solve(String s, int p) {
        char[] c = s.toCharArray();
        int cnt = 0;
        for (int x : c) {
            if (x == '-') {
                cnt++;
            }
        }
        int pick = p + 1;
        pick = cnt - pick + 1;
        if (pick <= cnt / 2) {
            pick *= 2;
        } else {
            pick = (pick - cnt / 2) * 2;
        }
        pick = cnt - pick + 1;
        int left = 0;
        int right = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '-') {
                if (i < c.length / 2) {
                    left++;
                } else {
                    right++;
                }
            }
        }
        if (pick <= right) {
            pick += left;
        } else {
            pick -= right;
        }

        int find = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '-') {
                find++;
                if (find == pick) {
                    return i;
                }
            }
        }
        return -1;
    }
}
