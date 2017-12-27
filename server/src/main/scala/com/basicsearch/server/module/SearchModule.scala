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
import com.twitter.inject.annotations.Flag
import com.twitter.util.Future

object SearchModule extends TwitterModule {

  flag(name = "role", default = "shard", help = "Role of the current server")

  val shards =
    flag[String](name = "shards", help = "Hostnames of all shards")

  @Singleton
  @Provides
  def searchService(
      @Flag("role") role: String): SearchService[Future, TextDocument] = {
    role match {
      case "master" =>
        val hosts = shards.get match {
          case Some(s) => s.split(",").toSeq
          case None =>
            throw new IllegalStateException("No shards specified for master.")
        }
        new RemoteSearchService(hosts.map(FinagleRestClient.apply))
      case _ => new LocalSearchService
    }

  }

}
