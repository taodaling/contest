package template.string.re;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegularExpression {
    State head;
    State[] all;

    public RegularExpression(State[] all, State head) {
        this.all = all;
        this.head = head;
    }

    public Matcher newMatcher() {
        return new Matcher(all, head);
    }

    @Override
    public String toString() {
        return Arrays.stream(all).map(Objects::toString).collect(Collectors.joining());
    }
}
