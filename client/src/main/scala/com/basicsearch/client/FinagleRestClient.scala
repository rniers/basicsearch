package com.basicsearch.client

import com.twitter.finagle.http.service.HttpResponseClassifier
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Method, Request, Response, Version}
import com.twitter.util.Future

class FinagleRestClient(service: Service[Request, Response])
    extends RestClient[Future] {

  /**
    * Add document to index
    *
    * @param key      - key, document will be referenced by
    * @param document - string which contains series of terms
    */
  override def put(key: String, document: String): Future[Unit] = {
    val request = Request(Version.Http11, Method.Post, s"/document/$key")
    request.setContentString(document)

    service(request).map(_ => ())
  }

  /**
    * Get document by it's key
    *
    * @param key - document key
    */
  override def get(key: String): Future[String] = {
    val request = Request(Version.Http11, Method.Get, s"/document/$key")

    service(request).map(_.getContentString())
  }

  /**
    * Get list of document keys, documents of which contain
    * all of passed terms
    *
    * @param terms - string containing search terms
    */
  override def search(terms: String): Future[Set[String]] = {
    val request =
      Request(Version.Http11, Method.Get, s"/document/search?terms=$terms")

    service(request).map(response =>
      FinagleRestClient.decode(response.getContentString()))
  }
}

object FinagleRestClient {

  protected def decode(resp: String): Set[String] = resp.split(",").toSet

  def apply(dest: String): FinagleRestClient = {
    val client = Http.client
      .withLabel("finagle-basicsearch-client")
      .withResponseClassifier(HttpResponseClassifier.ServerErrorsAsFailures)
      .newService(dest)

    new FinagleRestClient(client)
  }
}
