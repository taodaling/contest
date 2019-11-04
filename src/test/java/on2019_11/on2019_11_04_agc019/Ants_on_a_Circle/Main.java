package on2019_11.on2019_11_04_agc019.Ants_on_a_Circle;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_11/on2019_11_04_agc019/Ants_on_a_Circle/Ants on a Circle.json"))
			Assert.fail();
	}
}
