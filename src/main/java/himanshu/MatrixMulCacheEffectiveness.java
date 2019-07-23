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

@State(Scope.Benchmark)
public class MatrixMulCacheEffectiveness
{
  private static int NUM_ELEMS = 1000;

  private long[][] m1;
  private long[][] m2;
  private long[][] mul;

  public MatrixMulCacheEffectiveness() {
    m1 = new long[NUM_ELEMS][NUM_ELEMS];
    m2 = new long[NUM_ELEMS][NUM_ELEMS];
    mul = new long[NUM_ELEMS][NUM_ELEMS];
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public void simple(Blackhole blackhole)
  {
    for (int i = 0; i < NUM_ELEMS; i++) {
      for (int j = 0; j < NUM_ELEMS; j++) {
        for (int k = 0; k < NUM_ELEMS; k++) {
          mul[i][j] += (m1[i][k] * m2[k][j]);
        }
      }
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public void transposed(Blackhole blackhole)
  {
    for (int i = 0; i < NUM_ELEMS; i++) {
      for (int j = 0; j < NUM_ELEMS; j++) {
        for (int k = 0; k < NUM_ELEMS; k++) {
          mul[i][j] += (m1[i][k] * m2[j][k]);
        }
      }
    }
  }

  public static void main(String[] args) throws RunnerException
  {
    Options opt = new OptionsBuilder()
        .include(MatrixMulCacheEffectiveness.class.getSimpleName())
        .warmupIterations(5)
        .measurementIterations(10)
        .forks(1)
        .build();

    new Runner(opt).run();
  }
}
