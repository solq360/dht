package org.solq.dht.test.jmh;

import java.io.IOException;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.runner.RunnerException;
import org.solq.dht.test.Person;

public class MyBenchmark {

	// public static void main(String[] args) throws RunnerException,
	// IOException {
	// Options opt = new OptionsBuilder()
	// .include(Person.class.getSimpleName())
	// .build();
	//
	// new Runner(opt).run();
	//
	// }

	private static final String TEST = Person.class.getName();

	public static void main(String[] args) throws RunnerException, IOException {
		String[] s = getArguments(TEST, 5, 5000, 1);
		Main.main(s);
	}

	private static String[] getArguments(String className, int nRuns, int runForMilliseconds, int nThreads) {
		return new String[] { className, "-i", "" + nRuns, "-r", runForMilliseconds + "ms", "-t", "" + nThreads, "-w",
				"5000ms", "-wi", "3" };
	}
}