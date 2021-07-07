package net.minecraft.crash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;

public class CrashReportExtender {
	static final String SUFFIXFLAG = "â˜ƒâ˜ƒâ˜ƒâ˜ƒâ˜ƒSUFFIXFLAGâ˜ƒâ˜ƒâ˜ƒâ˜ƒâ˜ƒ";
	static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static List<ICrashCallable> crashCallables = Collections.synchronizedList(new ArrayList<>());

	public static void enhanceCrashReport(final CrashReport crashReport, final CrashReportCategory category) {
		for (final ICrashCallable call : crashCallables) {
			category.addDetail(call.getLabel(), call);
		}
	}

	public static String generateEnhancedStackTrace(final StackTraceElement[] stacktrace) {
		final Throwable t = new Throwable();
		t.setStackTrace(stacktrace);
		return generateEnhancedStackTrace(t, false);
	}

	public static String generateEnhancedStackTrace(final Throwable throwable, boolean header) {
		final String s = generateEnhancedStackTrace(throwable);
		return header ? s : s.substring(s.indexOf(LINE_SEPARATOR));
	}

	public static String generateEnhancedStackTrace(final Throwable throwable) {
		final ThrowableProxy proxy = new ThrowableProxy(throwable);
		final StringBuilder buffer = new StringBuilder();
		final TextRenderer textRenderer = new ExtraDataTextRenderer(PlainTextRenderer.getInstance());
		proxy.formatExtendedStackTraceTo(buffer, Collections.emptyList(),
				textRenderer, SUFFIXFLAG, LINE_SEPARATOR);
		return buffer.toString();
	}

	static class ThrowableProxy implements Serializable {
		private static final char EOL = '\n';
		private static final String EOL_STR = String.valueOf(EOL);
		private static final long serialVersionUID = -2752771578252251910L;
		private final ThrowableProxy causeProxy;
		private int commonElementCount;
		private final ExtendedStackTraceElement[] extendedStackTrace;
		private final String localizedMessage;
		private final String message;
		private final String name;
		private final ThrowableProxy[] suppressedProxies;
		private final transient Throwable throwable;

		public ThrowableProxy getCauseProxy() { return causeProxy; }

		public int getCommonElementCount() { return commonElementCount; }

		public ExtendedStackTraceElement[] getExtendedStackTrace() { return extendedStackTrace; }

		public String getLocalizedMessage() { return localizedMessage; }

		public String getMessage() { return message; }

		public String getName() { return name; }

		public ThrowableProxy[] getSuppressedProxies() { return suppressedProxies; }

		public Throwable getThrowable() { return throwable; }

		public StackTraceElement[] getStackTrace() {
			return this.throwable == null ? null : this.throwable.getStackTrace();
		}

		/**
		 * Constructs the wrapper for the Throwable that includes packaging data.
		 *
		 * @param throwable
		 *            The Throwable to wrap, must not be null.
		 */
		public ThrowableProxy(final Throwable throwable) {
			this(throwable, null);
		}

		/**
		 * Constructs the wrapper for the Throwable that includes packaging data.
		 *
		 * @param throwable
		 *            The Throwable to wrap, must not be null.
		 * @param visited
		 *            The set of visited suppressed exceptions.
		 */
		ThrowableProxy(final Throwable throwable, final Set<Throwable> visited) {
			this.throwable = throwable;
			this.name = throwable.getClass().getName();
			this.message = throwable.getMessage();
			this.localizedMessage = throwable.getLocalizedMessage();
			final Map<String, ThrowableProxyHelper.CacheEntry> map = new HashMap<>();
			final Stack<Class<?>> stack = StackLocatorUtil.getCurrentStackTrace();
			this.extendedStackTrace = ThrowableProxyHelper.toExtendedStackTrace(this, stack, map, null, throwable.getStackTrace());
			final Throwable throwableCause = throwable.getCause();
			final Set<Throwable> causeVisited = new HashSet<>(1);
			this.causeProxy = throwableCause == null ? null
					: new ThrowableProxy(throwable, stack, map, throwableCause,
							visited, causeVisited);
			this.suppressedProxies = ThrowableProxyHelper.toSuppressedProxies(throwable, visited);
		}

		/**
		 * Constructs the wrapper for a Throwable that is referenced as the cause by another Throwable.
		 *
		 * @param parent
		 *            The Throwable referencing this Throwable.
		 * @param stack
		 *            The Class stack.
		 * @param map
		 *            The cache containing the packaging data.
		 * @param cause
		 *            The Throwable to wrap.
		 * @param suppressedVisited
		 *            TODO
		 * @param causeVisited
		 *            TODO
		 */
		private ThrowableProxy(final Throwable parent, final Stack<Class<?>> stack,
				final Map<String, ThrowableProxyHelper.CacheEntry> map,
				final Throwable cause, final Set<Throwable> suppressedVisited,
				final Set<Throwable> causeVisited) {
			causeVisited.add(cause);
			this.throwable = cause;
			this.name = cause.getClass().getName();
			this.message = this.throwable.getMessage();
			this.localizedMessage = this.throwable.getLocalizedMessage();
			this.extendedStackTrace = ThrowableProxyHelper.toExtendedStackTrace(this, stack, map, parent.getStackTrace(), cause.getStackTrace());
			final Throwable causeCause = cause.getCause();
			this.causeProxy = causeCause == null || causeVisited.contains(causeCause) ? null
					: new ThrowableProxy(parent,
							stack, map, causeCause, suppressedVisited, causeVisited);
			this.suppressedProxies = ThrowableProxyHelper.toSuppressedProxies(cause, suppressedVisited);
		}

		/**
		 * Formats the stack trace including packaging information.
		 *
		 * @param sb
		 *            Destination.
		 * @param ignorePackages
		 *            List of packages to be ignored in the trace.
		 * @param textRenderer
		 *            The message renderer.
		 * @param suffix
		 *            Append this to the end of each stack frame.
		 * @param lineSeparator
		 *            The end-of-line separator.
		 */
		public void formatExtendedStackTraceTo(final StringBuilder sb, final List<String> ignorePackages, final TextRenderer textRenderer, final String suffix, final String lineSeparator) {
			ThrowableProxyRenderer.formatExtendedStackTraceTo(this, sb, ignorePackages, textRenderer, suffix, lineSeparator);
		}

		public void setCommonElementCount(int i) { commonElementCount = i; }

	}

	static class ExtraDataTextRenderer implements TextRenderer {
		private final TextRenderer wrapped;
		private final Optional<ITransformerAuditTrail> auditData;
		private ThreadLocal<TransformerContext> currentClass = new ThreadLocal<>();

		ExtraDataTextRenderer(final TextRenderer wrapped) {
			this.wrapped = wrapped;
			this.auditData = Optional
					.ofNullable(Launcher.INSTANCE)
					.map(Launcher::environment)
					.flatMap(env -> env.getProperty(IEnvironment.Keys.AUDITTRAIL.get()));
		}

		static class Launcher {
			public static Launcher INSTANCE;
			private final Environment environment;
			private final NameMappingServiceHandler nameMappingServiceHandler;

			public Launcher() {
				environment = null; // TODO
				nameMappingServiceHandler = null; // TODO
			}

			public Environment environment() {
				return this.environment;
			}

			Optional<BiFunction<INameMappingService.Domain, String, String>> findNameMapping(final String targetMapping) {
				return nameMappingServiceHandler.findNameTranslator(targetMapping);
			}
		}

		@Override
		public void render(final String input, final StringBuilder output, final String styleName) {
			if ("StackTraceElement.ClassName".equals(styleName)) {
				currentClass.set(new TransformerContext());
				currentClass.get().setClassName(input);
			} else if ("StackTraceElement.MethodName".equals(styleName)) {
				final TransformerContext transformerContext = currentClass.get();
				if (transformerContext != null) {
					transformerContext.setMethodName(input);
				}
			} else if ("Suffix".equals(styleName)) {
				final TransformerContext classContext = currentClass.get();
				currentClass.remove();
				if (classContext != null) {
					final Optional<String> auditLine = auditData.map(data -> data.getAuditString(classContext.getClassName()));
					wrapped.render(" {" + auditLine.orElse("") + "}", output, "StackTraceElement.Transformers");
				}
				return;
			}
			wrapped.render(input, output, styleName);
		}

		@Override
		public void render(final StringBuilder input, final StringBuilder output) {
			wrapped.render(input, output);
		}

		private static class TransformerContext {

			private String className;
			private String methodName;

			public void setClassName(final String className) {
				this.className = className;
			}

			public String getClassName() {
				return className;
			}

			public void setMethodName(final String methodName) {
				this.methodName = methodName;
			}

			public String getMethodName() {
				return methodName;
			}

			@Override
			public String toString() {
				return getClassName() + "." + getMethodName();
			}
		}
	}
}