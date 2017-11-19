package com.basicsearch.core.repository

import java.util.concurrent.ConcurrentHashMap

import cats.Id
import com.basicsearch.core.model.TextDocument

class InMemoryDocumentRepository extends DocumentRepository[Id, TextDocument] {

  private val store = new ConcurrentHashMap[String, String]()

  override def put(document: TextDocument): Unit =
    store.put(document.key, document.content)

  override def get(key: String): Option[TextDocument] =
    Option(store.get(key)).map(text => TextDocument(key, text))

  override def getAll(keys: Seq[String]): Seq[TextDocument] =
    keys.flatMap(k =>
      get(k) match {
        case Some(document) => List(document)
        case None           => List.empty
    })
}

object InMemoryDocumentRepository {
  def apply: InMemoryDocumentRepository = new InMemoryDocumentRepository()
}
