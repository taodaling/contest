package on2020_05.on2020_05_06_.Task;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_05/on2020_05_06_/Task/Task.json"))
			Assert.fail();
	}
}
