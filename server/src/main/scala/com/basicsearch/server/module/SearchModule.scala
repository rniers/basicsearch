package com.basicsearch.server.module

import javax.inject.Singleton

import com.basicsearch.client.FinagleRestClient
import com.basicsearch.core.model.TextDocument
import com.basicsearch.core.service.{
  LocalSearchService,
  RemoteSearchService,
  SearchService
}
import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import com.twitter.util.Future

object SearchModule extends TwitterModule {

  protected lazy val role =
    flag(name = "role", default = "shard", help = "Role of the current server")

  protected lazy val shards =
    flag[String](name = "shards", help = "Hostnames of all shards")

  @Singleton
  @Provides
  def searchService: SearchService[Future, TextDocument] = {
    role.get match {
      case Some("master") =>
        val hosts = shards.get match {
          case Some(s) => s.split(",").toSeq
          case None =>
            throw new IllegalStateException("No shards specified for master.")
        }
        new RemoteSearchService(hosts.map(host => FinagleRestClient(host)))
      case _ => new LocalSearchService
    }

  }

}
