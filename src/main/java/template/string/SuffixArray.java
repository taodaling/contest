package template.string;

import template.datastructure.Loop;

import java.util.Arrays;

public class SuffixArray {
    Suffix[] orderedSuffix;
    Suffix[] originalSuffix;
    int[] heights;
    char[] data;

    public SuffixArray(char[] s, int rangeFrom, int rangeTo) {
        this.data = s;

        int n = s.length;
        int range = n + 1;
        Loop<int[]> rankLoop = new Loop(new int[3][n + 1]);

        Suffix[] originalSuffix = new Suffix[n + 1];
        int[] firstRanks = rankLoop.get(0);
        for (int i = 0; i < n; i++) {
            originalSuffix[i] = new Suffix();
            originalSuffix[i].suffixStartIndex = i;
            firstRanks[i] = s[i] - rangeFrom + 1;
        }
        originalSuffix[n] = new Suffix();
        originalSuffix[n].suffixStartIndex = n;
        originalSuffix[n].rank = 0;
        Loop<Suffix[]> suffixLoop = new Loop(new Suffix[][]{
                originalSuffix.clone(), new Suffix[n + 1]
        });

        sort(suffixLoop.get(0), suffixLoop.get(1), rankLoop.get(0), rangeTo - rangeFrom + 2);
        assignRank(suffixLoop.turn(), rankLoop.get(0), rankLoop.get(0), rankLoop.turn());

        for (int i = 1; i < n; i <<= 1) {
            System.arraycopy(rankLoop.get(0), i, rankLoop.get(1), 0, range - i);
            Arrays.fill(rankLoop.get(1), range - i, range, 0);
            sort(suffixLoop.get(0), suffixLoop.turn(), rankLoop.get(1), range);
            sort(suffixLoop.get(0), suffixLoop.turn(), rankLoop.get(0), range);
            assignRank(suffixLoop.get(0), rankLoop.get(0), rankLoop.get(1), rankLoop.turn(2));
        }

        firstRanks = rankLoop.get(0);
        for (int i = 0; i < range; i++) {
            originalSuffix[i].rank = firstRanks[i];
        }

        this.originalSuffix = originalSuffix;
        this.orderedSuffix = suffixLoop.get();

        heights = new int[n + 1];
        for (int i = 0; i < n; i++) {
            Suffix suffix = originalSuffix[i];
            if (suffix.rank == 0) {
                heights[suffix.rank] = 0;
                continue;
            }
            int startIndex = suffix.suffixStartIndex;
            int former = startIndex - 1;
            int h = 0;
            if (former >= 0) {
                h = Math.max(h, heights[originalSuffix[former].rank] - 1);
            }
            int anotherStartIndex = orderedSuffix[suffix.rank - 1].suffixStartIndex;
            for (; startIndex + h < n && anotherStartIndex + h < n && s[startIndex + h] == s[anotherStartIndex + h]; h++)
                ;
            heights[suffix.rank] = h;
        }
    }

    private static void assignRank(Suffix[] seq, int[] firstKeys, int[] secondKeys, int[] rankOutput) {
        int cnt = 0;
        rankOutput[0] = 0;
        for (int i = 1, bound = seq.length; i < bound; i++) {
            int lastIndex = seq[i - 1].suffixStartIndex;
            int index = seq[i].suffixStartIndex;
            if (firstKeys[lastIndex] != firstKeys[index] ||
                    secondKeys[lastIndex] != secondKeys[index]) {
                cnt++;
            }
            rankOutput[index] = cnt;
        }
    }

    private static void sort(Suffix[] oldSeq, Suffix[] newSeq, int[] withRank, int range) {
        int[] counters = new int[range];
        for (int rank : withRank) {
            counters[rank]++;
        }
        int[] ranks = new int[range];
        ranks[0] = 0;
        for (int i = 1; i < range; i++) {
            ranks[i] = ranks[i - 1] + (counters[i] > 0 ? 1 : 0);
            counters[i] += counters[i - 1];
        }

        for (int i = oldSeq.length - 1; i >= 0; i--) {
            int newPos = --counters[withRank[oldSeq[i].suffixStartIndex]];
            newSeq[newPos] = oldSeq[i];
        }
    }

    /**
     * 获取第rank大的后缀，最小的后缀的排名为1
     */
    public Suffix getSuffixByRank(int rank) {
        return orderedSuffix[rank];
    }

    /**
     * 获取以startIndex开始的后缀对应的后缀对象
     */
    public Suffix getSuffixByStartIndex(int startIndex) {
        return originalSuffix[startIndex];
    }

    /**
     * 计算第i大的后缀和第i-1大的后缀的最长公共前缀长度
     */
    public int longestCommonPrefixOf(int i) {
        return heights[i];
    }



    public static class Suffix {
        private int suffixStartIndex;

        public int getSuffixStartIndex() {
            return suffixStartIndex;
        }

        public int getRank() {
            return rank;
        }

        private int rank;

//        @Override
//        public String toString() {
//            return String.valueOf(data, suffixStartIndex, data.length - suffixStartIndex);//suffixStartIndex + ":" + rank;
//        }
    }
}