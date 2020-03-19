package on2020_03.on2020_03_19_.TaskA;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_03/on2020_03_19_/TaskA/TaskA.json"))
			Assert.fail();
	}
}
