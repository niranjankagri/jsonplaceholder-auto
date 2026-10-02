package com.typicode.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;

import com.aventstack.extentreports.ExtentTest;

/**
 * Log4j2 appender that copies every log message to the Extent report entry of the running test,
 * so the existing {@code Util.getLogger()} calls show up as report steps:
 * "PASSED: ..." as a passed step, "FAILED: ..." as a failed step, anything else as info.
 *
 */
public class ExtentLogAppender extends AbstractAppender {

	private ExtentLogAppender() {
		super("Extent", null, null, true, Property.EMPTY_ARRAY);
	}

	/**
	 * Adds the appender to the root logger, next to the console and file appenders
	 * of log4j2.properties
	 */
	static void register() {
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		Configuration configuration = context.getConfiguration();

		ExtentLogAppender appender = new ExtentLogAppender();
		appender.start();
		configuration.addAppender(appender);
		configuration.getRootLogger().addAppender(appender, null, null);
		context.updateLoggers();
	}

	@Override
	public void append(LogEvent event) {
		ExtentTest test = ExtentReportListener.getCurrentTest();
		// Messages logged outside a test (e.g. at start-up) are not part of any report entry
		if(test == null) {
			return;
		}

		String message = event.getMessage().getFormattedMessage();
		if(message.startsWith("PASSED: ")) {
			test.pass(escapeHtml(message.substring("PASSED: ".length())));
		} else if(message.startsWith("FAILED: ")) {
			test.fail(escapeHtml(message.substring("FAILED: ".length())));
		} else if(message.contains("\n")) {
			// Multi-line messages such as response bodies keep their layout
			test.info("<pre>" + escapeHtml(message) + "</pre>");
		} else {
			test.info(escapeHtml(message));
		}
	}

	/**
	 * Extent renders step text as HTML, so response data must be escaped
	 * @param text : Text to be escaped
	 * @return : Text safe to show as HTML
	 */
	private static String escapeHtml(String text) {
		return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}
