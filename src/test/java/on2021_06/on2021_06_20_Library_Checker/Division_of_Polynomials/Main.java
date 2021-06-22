package on2021_06.on2021_06_20_Library_Checker.Division_of_Polynomials;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_06/on2021_06_20_Library_Checker/Division_of_Polynomials/Division of Polynomials.json"))
			Assert.fail();
	}
}
