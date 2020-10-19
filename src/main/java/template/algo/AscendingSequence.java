package template.algo;

import template.primitve.generated.datastructure.IntegerComparator;
import template.utils.CompareUtils;

import java.util.Arrays;

public final class AscendingSequence {
    public int[] data;

    public AscendingSequence(int[] data){
        this(data, false);
    }

    public AscendingSequence(int[] data, boolean sorted) {
        if (!sorted) {
            CompareUtils.insertSort(data, IntegerComparator.NATURE_ORDER, 0, data.length - 1);
        }
        this.data = data;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AscendingSequence)) {
            return false;
        }
        AscendingSequence as = (AscendingSequence) obj;
        return Arrays.equals(data, as.data);
    }
}
