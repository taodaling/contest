package on2020_09.on2020_09_04_Library_Checker.Discrete_Logarithm;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_09/on2020_09_04_Library_Checker/Discrete_Logarithm/Discrete Logarithm.json"))
			Assert.fail();
	}
}
