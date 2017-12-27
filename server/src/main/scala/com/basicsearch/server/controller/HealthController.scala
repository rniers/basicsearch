package com.basicsearch.server.controller

import javax.inject.Inject

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.annotations.Flag

class HealthController @Inject()(@Flag("role") role: String)
    extends Controller {

  get("/health") { _: Request =>
    version.BuildInfo.toJson
  }

  get("/") { _: Request =>
    s"$role"
  }
}
