package template;

public class SubsetGenerator {
    private int[] meanings = new int[33];
    private int[] bits = new int[33];
    private int remain;
    private int next;

    public void setSet(int set) {
        int bitCount = 0;
        while (set != 0) {
            meanings[bitCount] = set & -set;
            bits[bitCount] = 0;
            set -= meanings[bitCount];
            bitCount++;
        }
        remain = 1 << bitCount;
        next = 0;
    }

    public boolean hasNext() {
        return remain > 0;
    }

    private void consume() {
        remain = remain - 1;
        int i;
        for (i = 0; bits[i] == 1; i++) {
            bits[i] = 0;
            next -= meanings[i];
        }
        bits[i] = 1;
        next += meanings[i];
    }

    public int next() {
        int returned = next;
        consume();
        return returned;
    }
}