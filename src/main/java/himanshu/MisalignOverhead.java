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

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class MisalignOverhead
{
    private static int BUFFER_SIZE = 256*1024*1024;
    private static int NUM_ELEMS = BUFFER_SIZE/8;

    private LongBuffer aligned;
    private LongBuffer misaligned;

    public MisalignOverhead()
    {
      ByteBuffer bb = ByteBuffer.allocateDirect(BUFFER_SIZE);
      aligned = bb.asLongBuffer();

      bb = ByteBuffer.allocateDirect(BUFFER_SIZE+3);
      bb.get(); bb.get(); bb.get();
      misaligned = bb.asLongBuffer();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void alignedAccess(Blackhole blackhole)
    {
      for (int i = 0; i < NUM_ELEMS; i++) {
        blackhole.consume(aligned.get(i));
      }
    }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public void misalignedAccess(Blackhole blackhole)
  {
    for (int i = 0; i < NUM_ELEMS; i++) {
      blackhole.consume(misaligned.get(i));
    }
  }

    public static void main(String[] args) throws RunnerException
    {
      Options opt = new OptionsBuilder()
          .include(MisalignOverhead.class.getSimpleName())
          .warmupIterations(5)
          .measurementIterations(10)
          .forks(1)
          .build();

      new Runner(opt).run();
    }
}
