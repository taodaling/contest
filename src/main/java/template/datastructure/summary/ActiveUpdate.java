package template.datastructure.summary;

import template.utils.CloneSupportObject;
import template.utils.Update;

public class ActiveUpdate extends CloneSupportObject<ActiveUpdate> implements Update<ActiveUpdate> {
    long delta;

    @Override
    public void update(ActiveUpdate activeUpdate) {
        delta += activeUpdate.delta;
    }

    @Override
    public void clear() {
        delta = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(delta == 0);
    }

    public void asUpdate(long d) {
        this.delta = d;
    }

}
