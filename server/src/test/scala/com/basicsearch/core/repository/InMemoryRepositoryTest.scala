package com.basicsearch.core.repository

import com.basicsearch.core.model.TextDocument
import org.scalatest._

class InMemoryRepositoryTest extends FlatSpec with Matchers {

  "InMemoryRepository" should "return none if empty" in {
    val repo = new InMemoryDocumentRepository

    repo.get("key") shouldBe None
  }

  it should "return empty seq if empty" in {
    val repo = new InMemoryDocumentRepository

    repo.getAll(Seq.empty) === Seq.empty[TextDocument]
    repo.getAll(Seq("asdf", "qewr", "xcvcx")) === Seq.empty[TextDocument]
  }

  it should "store document" in {
    val repo = new InMemoryDocumentRepository
    val document = TextDocument("key", "document value")

    repo.put(document)

    repo.get("key") === document
  }

  it should "get all documents" in {
    val repo = new InMemoryDocumentRepository
    val document = TextDocument("key", "document value")
    val document2 = TextDocument("key2", "document value 2")
    val document3 = TextDocument("key3", "document value 3")

    repo.put(document)
    repo.put(document2)
    repo.put(document3)

    repo.getAll(Seq("key", "key2", "key3")).toSet === Set(document,
                                                          document2,
                                                          document3)
  }
}
