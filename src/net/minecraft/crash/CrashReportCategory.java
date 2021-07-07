package net.minecraft.crash;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

public class CrashReportCategory {
	private final CrashReport crashReport;
	private final String name;
	private final List<CrashReportCategory.Entry> children = Lists.newArrayList();
	private StackTraceElement[] stackTrace = new StackTraceElement[0];

	public CrashReportCategory(CrashReport report, String name) {
		this.crashReport = report;
		this.name = name;
	}

	/**
	 * Adds an additional section to this crash report category, resolved by calling the given callable.
	 * 
	 * If the given callable throws an exception, a detail containing that exception will be created instead.
	 */
	public CrashReportCategory addDetail(String nameIn, ICrashReportDetail<String> detail) {
		try {
			this.addDetail(nameIn, detail.call());
		} catch (Throwable throwable) {
			this.addCrashSectionThrowable(nameIn, throwable);
		}

		return this;
	}

	/**
	 * Adds a Crashreport section with the given name with the given value (convered .toString())
	 */
	public CrashReportCategory addDetail(String sectionName, Object value) {
		this.children.add(new CrashReportCategory.Entry(sectionName, value));
		return this;
	}

	/**
	 * Adds a Crashreport section with the given name with the given Throwable
	 */
	public void addCrashSectionThrowable(String sectionName, Throwable throwable) {
		this.addDetail(sectionName, throwable);
	}

	/**
	 * Resets our stack trace according to the current trace, pruning the deepest 3 entries. The parameter indicates how
	 * many additional deepest entries to prune. Returns the number of entries in the resulting pruned stack trace.
	 */
	public int getPrunedStackTrace(int size) {
		StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
		if (astacktraceelement.length <= 0) {
			return 0;
		} else {
			int len = astacktraceelement.length - 3 - size;
			if (len <= 0) len = astacktraceelement.length;
			this.stackTrace = new StackTraceElement[len];
			System.arraycopy(astacktraceelement, astacktraceelement.length - len, this.stackTrace, 0, this.stackTrace.length);
			return this.stackTrace.length;
		}
	}

	/**
	 * Do the deepest two elements of our saved stack trace match the given elements, in order from the deepest?
	 */
	public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement s1, StackTraceElement s2) {
		if (this.stackTrace.length != 0 && s1 != null) {
			StackTraceElement stacktraceelement = this.stackTrace[0];
			if (stacktraceelement.isNativeMethod() == s1.isNativeMethod() && stacktraceelement.getClassName().equals(s1.getClassName()) && stacktraceelement.getFileName().equals(s1.getFileName()) && stacktraceelement.getMethodName().equals(s1.getMethodName())) {
				if (s2 != null != this.stackTrace.length > 1) {
					return false;
				} else if (s2 != null && !this.stackTrace[1].equals(s2)) {
					return false;
				} else {
					this.stackTrace[0] = s1;
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Removes the given number entries from the bottom of the stack trace.
	 */
	public void trimStackTraceEntriesFromBottom(int amount) {
		StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - amount];
		System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
		this.stackTrace = astacktraceelement;
	}

	public void appendToStringBuilder(StringBuilder builder) {
		builder.append("-- ").append(this.name).append(" --\n");
		builder.append("Details:");

		for (CrashReportCategory.Entry crashreportcategory$entry : this.children) {
			builder.append("\n\t");
			builder.append(crashreportcategory$entry.getKey());
			builder.append(": ");
			builder.append(crashreportcategory$entry.getValue());
		}

		if (this.stackTrace != null && this.stackTrace.length > 0) {
			builder.append("\nStacktrace:");
			builder.append(CrashReportExtender.generateEnhancedStackTrace(this.stackTrace));
		}

	}

	static class Entry {
		private final String key;
		private final String value;

		public Entry(String key, @Nullable Object value) {
			this.key = key;
			if (value == null) {
				this.value = "~~NULL~~";
			} else if (value instanceof Throwable) {
				Throwable throwable = (Throwable) value;
				this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
			} else {
				this.value = value.toString();
			}

		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}
	}

}