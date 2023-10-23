#!/bin/sh

#VERSIONS="3.2.7.Final 3.5.0 999-SNAPSHOT"
VERSIONS="3.5.0 999-SNAPSHOT"

# Set max to use Runtime.getRuntime().availableProcessors()
THREADS="1"

# Benchmarks to run
if [ "$1" ]; then
    BENCHMARKS=$1
else
    BENCHMARKS="InterceptorBenchmark|SingleDecoratorBenchmark|ReflectionsBenchmark|SubclassInstantiationBenchmark|ApplicationScopedProxyInvocationBenchmark|RequestScopedProxyInvocationBenchmark|ContextProviderBenchmark|RequestContextActivationBenchmark|BeanManagerGetBeansBenchmark"
fi

echo "Versions: $VERSIONS"
echo "Benchmarks to run: $BENCHMARKS"

mvn clean

VERSIONS_ARRAY=$(echo $VERSIONS)

for i in $VERSIONS_ARRAY
do
  mvn package -Dquarkus.version=$i
  java -jar target/benchmarks.jar -t $THREADS -rf json -rff target/results-$i.json $BENCHMARKS
  java -cp target/benchmarks.jar io.quarkus.arc.benchmarks.chart.ChartGenerator target
done;
