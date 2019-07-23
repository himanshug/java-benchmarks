package himanshu;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 */
@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class MeanAggregationBenchmark
{
  @Param({"10", "100", "1000", "10000", "100000", "1000000"})
  private int n;

  @Benchmark
  public void sumcountalgo(Blackhole blackhole)
  {
    double sum = 0;
    long count = 0;

    for (int i = 1; i <= n; i++) {
      count++;
      sum += i;
    }

    double mean = sum/count;
    blackhole.consume(mean);
  }

  @Benchmark
  public void divbasedalgo(Blackhole blackhole)
  {
    double mean = 0;
    long count = 0;

    for (int i = 1; i <= n; i++) {
      count++;
      mean = mean + (i - mean)/count;
    }

    blackhole.consume(mean);
  }

//  public static void main(String[] args) throws RunnerException
//  {
//    Options opt = new OptionsBuilder()
//        .include(MeanAggregationBenchmark.class.getSimpleName())
//        .warmupIterations(5)
//        .measurementIterations(10)
//        .forks(1)
//        .build();
//
//    new Runner(opt).run();
//  }
}
