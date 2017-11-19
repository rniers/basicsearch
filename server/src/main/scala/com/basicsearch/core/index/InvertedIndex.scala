package com.basicsearch.core.index

import java.util.concurrent.atomic.AtomicInteger

import cats.Id
import com.basicsearch.core.model.{Document, TextDocument}

import scala.collection.concurrent.TrieMap

class InvertedIndex[T <: Document](tokenizer: Tokenizer[T])
    extends Index[Id, T] {

  private val store = new TrieMap[T#Term, Set[T#Key]]()
  private val count = new AtomicInteger(0)

  override def indexed: Int = count.get()

  override def add(document: T): Id[Unit] = {
    tokenizer
      .tokenize(document.content)
      .groupBy(identity)
      .keys
      .foreach { term =>
        store.get(term) match {
          case Some(links) => store.put(term, links + document.key)
          case None        => store.put(term, Set(document.key))
        }
      }
    count.incrementAndGet()
    ()
  }

  override def find(term: T#Content): Id[Set[T#Key]] = {
    val sets = tokenizer
      .tokenize(term)
      .toList
      .map(term => store.getOrElse(term, Set.empty))

    if (sets.nonEmpty) sets.reduce(_ intersect _)
    else Set.empty
  }

}

object InvertedIndex {

  def text: InvertedIndex[TextDocument] =
    new InvertedIndex(new TextTokenizer)

}
