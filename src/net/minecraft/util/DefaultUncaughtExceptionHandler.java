package net.minecraft.util;

import java.lang.Thread.UncaughtExceptionHandler;

import com.sunflow.logging.SunLogger;

public class DefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {
	private final SunLogger logger;

	public DefaultUncaughtExceptionHandler(SunLogger logger) {
		this.logger = logger;
	}

	@Override
	public void uncaughtException(Thread p_uncaughtException_1_, Throwable p_uncaughtException_2_) {
		this.logger.error("Caught previously unhandled exception :", p_uncaughtException_2_);
	}
}
