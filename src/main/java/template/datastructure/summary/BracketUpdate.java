package template.datastructure.summary;

import template.utils.CloneSupportObject;
import template.utils.Update;

public class BracketUpdate extends CloneSupportObject<BracketUpdate> implements Update<BracketUpdate> {
    public int size;
    public int x;

    public void asAdd(int x) {
        this.x = x;
        size = 1;
    }

    public void asDel(int x) {
        this.x = -x;
        size = -1;
    }

    public static BracketUpdate ofAdd(int x) {
        BracketUpdate ans = new BracketUpdate();
        ans.x = x;
        ans.size = 1;
        return ans;
    }

    public static BracketUpdate ofDel(int x) {
        BracketUpdate ans = new BracketUpdate();
        ans.x = -x;
        ans.size = -1;
        return ans;
    }

    @Override
    public void update(BracketUpdate upd) {
        x += upd.x;
        size += upd.size;
    }

    @Override
    public void clear() {
        x = size = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(x == 0 && size == 0);
    }
}
