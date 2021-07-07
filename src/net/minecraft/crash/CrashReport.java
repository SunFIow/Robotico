package net.minecraft.crash;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;

public class CrashReport {
	private final String description;
	private final Throwable cause;
	private final CrashReportCategory systemDetailsCategory = new CrashReportCategory(this, "System Details");
	private final List<CrashReportCategory> crashReportSections = Lists.newArrayList();
	private File crashReportFile;
	private boolean firstCategoryInCrashReport = true;
	private StackTraceElement[] stacktrace = new StackTraceElement[0];

	public CrashReport(String descriptionIn, Throwable causeThrowable) {
		this.description = descriptionIn;
		this.cause = causeThrowable;
		this.populateEnvironment();
	}

	/**
	 * Populates this crash report with initial information about the running server and operating system / java
	 * environment
	 */
	private void populateEnvironment() {
		this.systemDetailsCategory.addDetail("Robotico Version", () -> {
			return SharedConstants.getVersion().getName();
		});
		this.systemDetailsCategory.addDetail("Robotico Version ID", () -> {
			return SharedConstants.getVersion().getId();
		});
		this.systemDetailsCategory.addDetail("Operating System", () -> {
			return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
		});
		this.systemDetailsCategory.addDetail("Java Version", () -> {
			return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
		});
		this.systemDetailsCategory.addDetail("Java VM Version", () -> {
			return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
		});
		this.systemDetailsCategory.addDetail("Memory", () -> {
			Runtime runtime = Runtime.getRuntime();
			long i = runtime.maxMemory();
			long j = runtime.totalMemory();
			long k = runtime.freeMemory();
			long l = i / 1024L / 1024L;
			long i1 = j / 1024L / 1024L;
			long j1 = k / 1024L / 1024L;
			return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
		});
		this.systemDetailsCategory.addDetail("CPUs", Runtime.getRuntime().availableProcessors());
		this.systemDetailsCategory.addDetail("JVM Flags", () -> {
			List<String> list = Util.getJvmFlags().collect(Collectors.toList());
			return String.format("%d total; %s", list.size(), list.stream().collect(Collectors.joining(" ")));
		});
		CrashReportExtender.enhanceCrashReport(this, this.systemDetailsCategory);
	}

	/**
	 * Creates a crash report for the exception
	 */
	public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn) {
		while (causeIn instanceof CompletionException && causeIn.getCause() != null) {
			causeIn = causeIn.getCause();
		}

		CrashReport crashreport;
		if (causeIn instanceof ReportedException) {
			crashreport = ((ReportedException) causeIn).getCrashReport();
		} else {
			crashreport = new CrashReport(descriptionIn, causeIn);
		}

		return crashreport;
	}

	/**
	 * Creates a CrashReportCategory
	 */
	public CrashReportCategory makeCategory(String name) {
		return this.makeCategoryDepth(name, 1);
	}

	/**
	 * Creates a CrashReportCategory for the given stack trace depth
	 */
	public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength) {
		CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
		if (this.firstCategoryInCrashReport) {
			int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
			StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
			StackTraceElement stacktraceelement = null;
			StackTraceElement stacktraceelement1 = null;
			int j = astacktraceelement.length - i;
			if (j < 0) {
				System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
			}

			if (astacktraceelement != null && 0 <= j && j < astacktraceelement.length) {
				stacktraceelement = astacktraceelement[j];
				if (astacktraceelement.length + 1 - i < astacktraceelement.length) {
					stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i];
				}
			}

			this.firstCategoryInCrashReport = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
			if (i > 0 && !this.crashReportSections.isEmpty()) {
				CrashReportCategory crashreportcategory1 = this.crashReportSections.get(this.crashReportSections.size() - 1);
				crashreportcategory1.trimStackTraceEntriesFromBottom(i);
			} else if (astacktraceelement != null && astacktraceelement.length >= i && 0 <= j && j < astacktraceelement.length) {
				this.stacktrace = new StackTraceElement[j];
				System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
			} else {
				this.firstCategoryInCrashReport = false;
			}
		}

		this.crashReportSections.add(crashreportcategory);
		return crashreportcategory;
	}

	/**
	 * Returns the description of the Crash Report.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the Throwable object that is the cause for the crash and Crash Report.
	 */
	public Throwable getCrashCause() {
		return this.cause;
	}

}