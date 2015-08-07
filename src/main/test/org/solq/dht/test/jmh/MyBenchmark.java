package org.solq.dht.test.jmh;

import java.io.IOException;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

 
public class MyBenchmark {

	public static void main(String[] args) throws RunnerException, IOException {
 		Options opt = new OptionsBuilder()
               .include(Person.class.getSimpleName())
                .build();
  
 		new Runner(opt).run();
 		
 	}
 
}