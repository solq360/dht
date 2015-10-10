package org.solq.dht.jmh;

import java.io.IOException;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class MyBenchmark {
	@GenerateMicroBenchmark
	public void wellHelloThere() {
 	}

	private static final String TEST = ".*" +  Person.class.getSimpleName() + ".*";

	public static void main(String[] args) throws RunnerException, IOException {
//		String[] s = getArguments(TEST, 5, 5000, 1);
//		Main.main(s);
		
		Options opt = new OptionsBuilder().include(".*" + MyBenchmark.class.getSimpleName() + ".*").forks(1)
				.build();
 
		new Runner(opt).run();
	}

	private static String[] getArguments(String className, int nRuns, int runForMilliseconds, int nThreads) {
		return new String[] { className, "-i", "" + nRuns, "-r", runForMilliseconds + "ms", "-t", "" + nThreads, "-w",
				"5000ms", "-wi", "3" };
	}
}