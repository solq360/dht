package org.solq.dht.jmh;

import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Warmup(iterations = AbstractMicrobenchmark.DEFAULT_WARMUP_ITERATIONS)
@Measurement(iterations = AbstractMicrobenchmark.DEFAULT_MEASURE_ITERATIONS)
@Fork(AbstractMicrobenchmark.DEFAULT_FORKS)
@State(Scope.Thread)
public class AbstractMicrobenchmark {

	protected static final int DEFAULT_WARMUP_ITERATIONS = 10;
	protected static final int DEFAULT_MEASURE_ITERATIONS = 10;
	protected static final int DEFAULT_FORKS = 2;

	protected static final String[] JVM_ARGS = { "-server", "-dsa", "-da", "-Xms768m", "-Xmx768m",
			"-XX:MaxDirectMemorySize=768m", "-XX:+AggressiveOpts", "-XX:+UseBiasedLocking",
			"-XX:+UseFastAccessorMethods", "-XX:+UseStringCache", "-XX:+OptimizeStringConcat",
			"-XX:+HeapDumpOnOutOfMemoryError", "-Dio.netty.noResourceLeakDetection", "-Dharness.executor=CUSTOM",
			"-Dharness.executor.class=io.netty.microbench.util.AbstractMicrobenchmark$HarnessExecutor" };

	// @Test
	public void run() throws Exception {
		// String className = getClass().getSimpleName();
		String className = Person.class.getSimpleName();

		ChainedOptionsBuilder runnerOptions = new OptionsBuilder().include(".*" + className + ".*").jvmArgs(JVM_ARGS);

		// if (getWarmupIterations() > 0) {
		// runnerOptions.warmupIterations(getWarmupIterations());
		// }
		//
		// if (getMeasureIterations() > 0) {
		// runnerOptions.measurementIterations(getMeasureIterations());
		// }
		//
		// if (getForks() > 0) {
		// runnerOptions.forks(getForks());
		// }
		//
		// if (getReportDir() != null) {
		// String filePath = getReportDir() + className + ".json";
		// File file = new File(filePath);
		// if (file.exists()) {
		// file.delete();
		// } else {
		// file.getParentFile().mkdirs();
		// file.createNewFile();
		// }
		//
		// runnerOptions.resultFormat(ResultFormatType.JSON);
		// runnerOptions.result(filePath);
		// }

		new Runner(runnerOptions.build()).run();
	}

	// protected int getWarmupIterations() {
	// return SystemPropertyUtil.getInt("warmupIterations", -1);
	// }
	//
	// protected int getMeasureIterations() {
	// return SystemPropertyUtil.getInt("measureIterations", -1);
	// }
	//
	// protected int getForks() {
	// return SystemPropertyUtil.getInt("forks", -1);
	// }
	//
	// protected String getReportDir() {
	// return SystemPropertyUtil.get("perfReportDir");
	// }
}