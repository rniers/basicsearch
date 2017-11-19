package com.basicsearch.core.service

import cats.Id
import com.basicsearch.core.index.InvertedIndex
import com.basicsearch.core.model.TextDocument
import com.basicsearch.core.repository.InMemoryDocumentRepository
import org.scalatest._

class SearchServiceTest extends FlatSpec with Matchers {

  import SearchService._

  "Search service" should "store documnent" in {
    val service = new SearchServiceImpl[Id, Id, TextDocument](
      new InMemoryDocumentRepository,
      InvertedIndex.text)
    val document = TextDocument("key", "some document value")
    service.put(document)

    service.get("key").get === document
  }

  it should "return none on missing document" in {
    val service = new SearchServiceImpl[Id, Id, TextDocument](
      new InMemoryDocumentRepository,
      InvertedIndex.text)
    val document = TextDocument("key", "some document value")
    service.put(document)

    service.get("key2") === None
  }

  it should "find document keys" in {
    val service = new SearchServiceImpl[Id, Id, TextDocument](
      new InMemoryDocumentRepository,
      InvertedIndex.text)
    val document = TextDocument("key", "some document value")
    service.put(document)

    val keys: Set[String] = service.search("document")
    keys should contain allElementsOf Set("key")
  }


  // todo: remote service test
}
