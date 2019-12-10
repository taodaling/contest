package template.math;

import org.junit.Assert;
import org.junit.Test;

public class ModPrimeRootTest {
    Modular mod = new Modular(998244353);

    @Test
    public void test1() {
        ModPrimeRoot root = new ModPrimeRoot(mod);
        Assert.assertEquals(2, root.root(4, 2));
        Assert.assertEquals(3, root.root(9, 2));
        Assert.assertEquals(2, root.root(8, 3));
    }
}