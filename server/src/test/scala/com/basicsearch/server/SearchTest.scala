package com.basicsearch.server

import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class SearchTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new Server)

  test("Search controller should store document") {
    val key = "key"
    val documentText = "some arbitrary document text"

    server.httpPost(path = s"/document/$key",
                    postBody = documentText,
                    andExpect = Status.Ok)
  }

  test("Search controller should return document by key") {

    val key = "key"
    val documentText = "some arbitrary document text"

    server.httpPost(path = s"/document/$key",
                    postBody = documentText,
                    andExpect = Status.Ok)

    val response =
      server.httpGet(path = s"/document/$key", andExpect = Status.Ok)
    assert(response.contentString == documentText)
  }

  test("Search controller should find document by keys") {
    val key = "key"
    val documentText = "some arbitrary document text"

    server.httpPost(path = s"/document/$key",
                    postBody = documentText,
                    andExpect = Status.Ok)

    val response =
      server.httpGet(path = s"/document/search?query=document",
                     andExpect = Status.Ok)
    assert(response.contentString == "key")
  }

}
