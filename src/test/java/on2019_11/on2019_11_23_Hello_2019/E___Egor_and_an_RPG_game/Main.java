package on2019_11.on2019_11_23_Hello_2019.E___Egor_and_an_RPG_game;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2019_11/on2019_11_23_Hello_2019/E___Egor_and_an_RPG_game/E - Egor and an RPG game.json"))
			Assert.fail();
	}
}
