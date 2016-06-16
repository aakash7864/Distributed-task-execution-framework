#!/bin/bash

java -jar Test1.jar perf 1 &
pid1=$!
wait $pid1

java -jar Test1.jar perf 2 &
pid2=$!
wait $pid2

java -jar Test1.jar perf 4 &
pid3=$!
wait $pid3

java -jar Test1.jar perf 8 &
pid4=$!
wait $pid4

java -jar Test1.jar perf 16 &
pid5=$!
wait $pid5



java -jar Test1.jar Sleep 1 &
pid11=$!
wait $pid11

java -jar Test1.jar Sleep 2 &
pid21=$!
wait $pid21

java -jar Test1.jar Sleep 4 &
pid31=$!
wait $pid31

java -jar Test1.jar Sleep 8 &
pid41=$!
wait $pid41

java -jar Test1.jar Sleep 16 &
pid51=$!
wait $pid51


java -jar Test1.jar Sleep1 1 &
pid111=$!
wait $pid111

java -jar Test1.jar Sleep1 2 &
pid211=$!
wait $pid211

java -jar Test1.jar Sleep1 4 &
pid311=$!
wait $pid311

java -jar Test1.jar Sleep1 8 &
pid411=$!
wait $pid411

java -jar Test1.jar Sleep1 16 &
pid511=$!
wait $pid511




java -jar Test1.jar Sleep10 1 &
pid1111=$!
wait $pid1111

java -jar Test1.jar Sleep10 2 &
pid2111=$!
wait $pid2111

java -jar Test1.jar Sleep10 4 &
pid3111=$!
wait $pid3111

java -jar Test1.jar Sleep10 8 &
pid4111=$!
wait $pid4111

java -jar Test1.jar Sleep10 16 &
pid5111=$!
wait $pid5111



echo "All Process Completed"
