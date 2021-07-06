package on2021_06.on2021_06_25_Library_Checker.Multivariate_Convolution;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_06/on2021_06_25_Library_Checker/Multivariate_Convolution/Multivariate Convolution.json"))
			Assert.fail();
	}
}
