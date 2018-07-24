package com.dishbreak.leaderelection

import java.util.UUID.randomUUID

import org.apache.curator.framework.recipes.leader.{LeaderSelector, LeaderSelectorListenerAdapter}
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry

class LeaderElectorListener extends LeaderSelectorListenerAdapter {
  var running: Boolean = true

  override def takeLeadership(curatorFramework: CuratorFramework): Unit = {
    println("Gulp. I'm the leader now.")

    while (running) {
      Thread.sleep(1000)
    }
  }

  def close(): Unit = {
    println("Closing leader listener")
    this.running = false
  }
}

class LeaderElector(uuid: String, zookeeperUrl: String) {
  val curatorFramework: CuratorFramework = CuratorFrameworkFactory
    .newClient(zookeeperUrl, new ExponentialBackoffRetry(1000,3))

  val mutexPath = "/dishbreak"

  val listener: LeaderElectorListener = new LeaderElectorListener

  val leaderSelector: LeaderSelector = new LeaderSelector(
    curatorFramework, mutexPath, listener
  )

  def start(): Unit = {
    sys.ShutdownHookThread {
      println("Shutting down.")
      listener.close()
      leaderSelector.close()
      curatorFramework.close()
    }

    curatorFramework.start()
    curatorFramework.create.creatingParentContainersIfNeeded.forPath("/dishbreak/" + uuid, "hello".getBytes())
    leaderSelector.start()

    println("Started!")
  }
}

object LeaderElector {
  private val zookeeperUrl = System.getProperty("zookeeper.url")

  def main(args: Array[String]): Unit = {
    println("Hello World!")
    val uniqueId = randomUUID().toString

    val elector = new LeaderElector(uniqueId, zookeeperUrl)
    elector.start()

    while (true) {
      Thread.sleep(1000)
    }
  }
}