package template.string;

public abstract class AbstractIntSequence implements IntSequence {
    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("[");
        for (int i = 0; i < length(); i++) {
            ans.append(get(i)).append(',');
        }
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append("]");
        return ans.toString();
    }
}
