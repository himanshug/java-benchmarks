package himanshu;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 */
@State(Scope.Benchmark)
public class SynchronizeOverhead
{
  private int n = 1000000;

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void unsynchronizedAdd(Blackhole blackhole)
  {
    for (int i = 0; i < n; i++) {
      blackhole.consume(1);
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void synchronizedAdd(Blackhole blackhole)
  {
    for (int i = 0; i < n; i++) {
      synchronized(this) {
        blackhole.consume(1);
      }
    }
  }

  public static void main(String[] args) throws RunnerException
  {
    Options opt = new OptionsBuilder()
        .include(SynchronizeOverhead.class.getSimpleName())
        .warmupIterations(5)
        .measurementIterations(10)
        .forks(1)
        .build();

    new Runner(opt).run();
  }
}
