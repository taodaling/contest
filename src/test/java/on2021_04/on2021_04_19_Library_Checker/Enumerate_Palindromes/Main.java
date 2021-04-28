package on2021_04.on2021_04_19_Library_Checker.Enumerate_Palindromes;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_04/on2021_04_19_Library_Checker/Enumerate_Palindromes/Enumerate Palindromes.json"))
			Assert.fail();
	}
}
