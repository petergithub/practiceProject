package demo.javabasics.vs;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.pu.utils.Utils;
import org.pu.utils.Utils.ExecutionTimer;


/**
 * test StringBuilder and StringBuffer
 * 
 * @author Shang Pu
 * @version Date: Apr 20, 2012 1:22:07 PM
 */
public class StringBuilderVsStringBuffer {
	protected static final Logger logger = Logger.getLogger(CallBackImpl.class);

	public static void main(String[] args) {
		long totalStringBufferTest = 0;
		long totalStringBuilderTest = 0;
		for (int i = 0; i < 10; i++) {
			long millisBuffer = Utils.getExecuteTime(new CallBackImpl(
					new StringBuffer()));
			logger.info("millisBuffer = " + millisBuffer);
			totalStringBufferTest += millisBuffer;

			long millisBuilder = Utils.getExecuteTime(new CallBackImpl(
					new StringBuilder()));
			logger.info("millisBuilder = " + millisBuilder);
			totalStringBuilderTest += millisBuilder;
		}

		logger.info("Total time in milliseconds for StringBuffer test:"
				+ totalStringBufferTest);
		logger.info("Total time in milliseconds for StringBuilder test:"
				+ totalStringBuilderTest);
	}
}

class CallBackImpl implements ExecutionTimer {
	protected static final Logger logger = Logger.getLogger(CallBackImpl.class);
	Appendable appendable;

	public CallBackImpl() {
	}

	public CallBackImpl(Appendable appendable) {
		this.appendable = appendable;
	}

	public void execute() {
		try {
			for (int i = 0; i < 5000000; i++) {
				appendable.append('a');
			}
		} catch (IOException e) {
			logger.error("Exception in AbstractCallBack.execute()", e);
		}
	}
}
