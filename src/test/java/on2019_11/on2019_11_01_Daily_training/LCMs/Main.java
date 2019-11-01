package on2019_11.on2019_11_01_Daily_training.LCMs;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_11/on2019_11_01_Daily_training/LCMs/LCMs.json"))
			Assert.fail();
	}
}
