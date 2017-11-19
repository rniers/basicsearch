package com.basicsearch.server.controller

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class HealthController extends Controller {

  get("/health") { _: Request =>
    "Ok"
  }
}
