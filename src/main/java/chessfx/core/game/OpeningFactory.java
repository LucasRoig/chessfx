package chessfx.core.game;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OpeningFactory {
	/**
	 * Renvoie une nouvelle ouverture vide
	 * @return une nouvelle ouverture vide
	 */
	public IOpening getOpening() {
		ApplicationContext ap = new ClassPathXmlApplicationContext("beans.xml");
		IOpening g = (IOpening) ap.getBean("opening");
		((ConfigurableApplicationContext) ap).close();
		return g;
	}
}
