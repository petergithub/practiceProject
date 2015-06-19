package doing;

import java.util.HashSet;
import java.util.function.Consumer;

import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdkFeature extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(JdkFeature.class);

	public void testSetForeach() {
		HashSet<String> set = new HashSet<>();
		set.add("a");
		set.add("b");
		set.add("c");
		Consumer<? super String> action = new Consumer<String>() {
			@Override
			public void accept(String t) {
				log.info("t = {}", t);
			}
		};
		set.forEach(action);
	}
}
