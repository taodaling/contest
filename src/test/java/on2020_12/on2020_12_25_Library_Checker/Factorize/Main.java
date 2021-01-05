package on2020_12.on2020_12_25_Library_Checker.Factorize;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_12/on2020_12_25_Library_Checker/Factorize/Factorize.json"))
			Assert.fail();
	}
}
