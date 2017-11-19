package com.basicsearch.server

import com.basicsearch.server.controller.{HealthController, SearchController}
import com.basicsearch.server.module.SearchModule
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{
  CommonFilters,
  LoggingMDCFilter,
  TraceIdMDCFilter
}
import com.twitter.finatra.http.routing.HttpRouter

object App extends Server

class Server extends HttpServer {

  override val modules = Seq(
    SearchModule
  )

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[HealthController]
      .add[SearchController]
  }
}
