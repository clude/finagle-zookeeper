package com.twitter.finagle.exp.zookeeper

import com.twitter.finagle.exp.zookeeper.transport.{NettyTrans, ZkTransport}
import com.twitter.finagle.client.{Bridge, DefaultClient}
import com.twitter.finagle.{Client, ServiceFactory, Name}
import com.twitter.finagle.exp.zookeeper.client.{ZkDispatcher, ZkClient}
import com.twitter.io.Buf

/**
 * -- ZkDispatcher
 * The zookeeper protocol allows the server to send notifications
 * with model is not basically supported by Finagle, that's the reason
 * why we are using a dedicated dispatcher based on SerialClientDispatcher and
 * GenSerialClientDispatcher.
 *
 * -- ZkTransport
 * We need to frame Rep here, that's why we use ZkTransport
 * which is responsible of queueing every incoming responses
 * and reading new ones from the transport if queue is empty.
 */
object ZooKeeperClient extends DefaultClient[Request, Response](
  name = "zookeeper",
  endpointer = Bridge[Buf, Buf, Request, Response](
    NettyTrans(_, _) map { new ZkTransport(_) }, new ZkDispatcher(_)))

object ZooKeeper extends Client[Request, Response] {
  def newClient(name: Name, label: String): ServiceFactory[Request, Response] =
    ZooKeeperClient.newClient(name, label)

  def newRichClient(dest: String): ZkClient =
    new ZkClient(newClient(dest))
}