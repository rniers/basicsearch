package com.basicsearch.server.controller

import com.basicsearch.core.model.TextDocument
import com.basicsearch.core.service.SearchService
import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future

class SearchController @Inject()(service: SearchService[Future, TextDocument])
    extends Controller {

  get("/document/:id") { request: Request =>
    service.get(request.params("id")).map(_.map(_.content))
  }

  post("/document/:id") { request: Request =>
    val key = request.params("id")
    val text = request.getContentString()
    service.put(TextDocument(key, text))
  }

  get("/document/search") { request: Request =>
    service.search(request.params("query")).map(_.mkString(","))
  }

}
