package on2020_09.on2020_09_29_Library_Checker.Many_A___B0;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_09/on2020_09_29_Library_Checker/Many_A___B0/Many A + B.json"))
			Assert.fail();
	}
}