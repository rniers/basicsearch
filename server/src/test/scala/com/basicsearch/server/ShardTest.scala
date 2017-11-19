package com.basicsearch.server

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class ShardTest extends FeatureTest {

  val shard1 = new EmbeddedHttpServer(new Server())

  val shard2 = new EmbeddedHttpServer(new Server())

  override val server = new EmbeddedHttpServer(
    twitterServer = new Server(),
    flags = Map(
      "role" -> "master",
      "shards" -> Seq(shard1.externalHttpHostAndPort,
                      shard2.externalHttpHostAndPort).mkString(",")
    )
  )

  // todo: implement
}
