package chessfx.core.game;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GameFactory {
	public IGame getGame() {
		ApplicationContext ap = new ClassPathXmlApplicationContext("beans.xml");
		IGame g = (IGame) ap.getBean("game");
		((ConfigurableApplicationContext) ap).close();
		return g;
	}
}
