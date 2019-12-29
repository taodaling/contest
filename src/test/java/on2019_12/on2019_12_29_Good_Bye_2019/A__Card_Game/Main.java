package on2019_12.on2019_12_29_Good_Bye_2019.A__Card_Game;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_12/on2019_12_29_Good_Bye_2019/A__Card_Game/A. Card Game.json"))
			Assert.fail();
	}
}
