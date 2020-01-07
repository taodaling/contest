package on2020_01.on2020_01_06_Hello_2020.F__New_Year_and_Social_Network;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_01/on2020_01_06_Hello_2020/F__New_Year_and_Social_Network/F. New Year and Social Network.json"))
			Assert.fail();
	}
}
