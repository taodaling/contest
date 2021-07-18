package on2021_07.on2021_07_17_Virtual_Judge___UVALive.Mining_Your_Own_Business;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_07/on2021_07_17_Virtual_Judge___UVALive/Mining_Your_Own_Business/Mining Your Own Business.json"))
			Assert.fail();
	}
}
