# ArC microbenchmarks

Collection of ArC microbenchmarks written using JMH.
Not peer reviewed or anything, so take the results with a grain of salt ;-)

## Running

Use `run-benchmarks.sh`.

## Running manually with Async Profiler

First, build the benchmark JAR: `mvn clean package`.
You will find it's a plain old JMH application, try e.g. `java -jar target/benchmarks.jar -h`.

Then, download Async Profiler from https://github.com/jvm-profiling-tools/async-profiler.
To get the 3.0.0 release for x86_64 Linux, you can run:

```bash
wget https://github.com/async-profiler/async-profiler/releases/download/v3.0/async-profiler-3.0-linux-x64.tar.gz
tar xfz async-profiler-3.0-linux-x64.tar.gz
```

> Note that the last JMH release, 1.37, is tested with Async Profiler 3.0, so that's what we recommend above.
> Async Profiler 4.2 also works.

JMH has support for Async Profiler built in, you just have to make sure it can be found.
One easy way is to put the Async Profiler native library to the Java native library path using `-Djava.library.path`.

Also, there are some preparations necessary before running Async Profiler.
Please read https://github.com/async-profiler/async-profiler/blob/master/docs/GettingStarted.md

```bash
java -Djava.library.path=./async-profiler-3.0-linux-x64/lib -jar target/benchmarks.jar -f 1 -prof async:output=flamegraph SingleInterceptorBenchmark
```

Here, we use `-f 1` to use only one JVM fork.
With N forks, we would unfortunately _not_ get N reports from Async Profiler, neither would we get a single combined report.
The last one would win, the previous are just lost.
