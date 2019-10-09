package on2019_10.on2019_10_09_.Task0;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_10/on2019_10_09_/Task0/Task.json"))
			Assert.fail();
	}
}
