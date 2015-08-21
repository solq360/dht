package org.solq.dht.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 3, jvmArgsAppend = { "-server", "-disablesystemassertions" })
public class Person {
	private String name;
	private int age;

	public static Person of(String name, int age) {
		Person result = new Person();
		result.name = name;
		result.age = age;
		return result;
	}

	public static List<Person> ofList() {
		List<Person> result = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			result.add(of("a" + i, i % 5));
		}
		return result;
	}

	@Warmup(iterations = 10, time = 3, timeUnit = TimeUnit.SECONDS)
	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public static void main(String[] args) {

	}

}
