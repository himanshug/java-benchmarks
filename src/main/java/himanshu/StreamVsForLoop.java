package himanshu;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class StreamVsForLoop
{
  @Param({"10", "100", "1000", "10000", "100000", "1000000"})
  private int n;

  private Set<StringHolder> stringSuppliers;

  @Setup
  public void setUp()
  {
    stringSuppliers = new HashSet<>(n);
    for (int i = 0; i < n; i++) {
      stringSuppliers.add(new StringHolder(String.valueOf(i)));
    }
  }

  @Benchmark
  public void stream(Blackhole blackhole)
  {
    Set<String> ss = stringSuppliers.stream().map(StringHolder::get).collect(Collectors.toSet());
    blackhole.consume(ss);
  }

  @Benchmark
  public void forloop(Blackhole blackhole)
  {
    Set<String> ss = new HashSet<>(stringSuppliers.size());
    for (StringHolder sh : stringSuppliers) {
      ss.add(sh.get());
    }

    blackhole.consume(ss);
  }

  static class StringHolder
  {
    private final String str;

    public StringHolder(String str)
    {
      this.str = str;
    }

    public String get()
    {
      return str;
    }
  }
}
