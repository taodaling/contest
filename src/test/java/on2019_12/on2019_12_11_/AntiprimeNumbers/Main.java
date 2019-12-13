package on2019_12.on2019_12_11_.AntiprimeNumbers;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_12/on2019_12_11_/AntiprimeNumbers/AntiprimeNumbers.json"))
			Assert.fail();
	}
}
