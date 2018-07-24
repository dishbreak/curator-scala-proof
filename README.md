# Curator Proof of Concept

## Motivation

This is a quick and dirty proof-of-concept that does the following:

* Proves the use of Apache Curator for distributed leader election.

## Before You Begin

Start a Zookeeper instance. The easiest way to do this is through Docker. 

```bash
docker run -p "2181:2181" zookeeper:3.4
```

Note the version is important! It's the one that's compatible with our version of Curator.

## Compiling

To compile the jar, use the command:

```bash
sbt assembly
```

## Running 

Open a terminal, and type the command:

```bash
java -Dzookeeper.url=192.168.99.100:2181 -jar target/scala-2.10/CuratorFramework-assembly-0.1.jar
```

This first instance should come up as the leader.

```
Started!
Gulp. I'm the leader now.
```

Launch the same application two more times in two separate windows. 
You'll see that both have the same output. Note that neither is the leader. 
If you use `Ctrl+C` to kill the first process, another process will become the leader. 