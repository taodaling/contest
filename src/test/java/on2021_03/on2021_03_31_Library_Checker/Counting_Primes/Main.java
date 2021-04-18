package on2021_03.on2021_03_31_Library_Checker.Counting_Primes;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_03/on2021_03_31_Library_Checker/Counting_Primes/Counting Primes.json"))
			Assert.fail();
	}
}
