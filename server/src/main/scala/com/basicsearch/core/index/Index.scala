package com.basicsearch.core.index

import com.basicsearch.core.model.Document

trait Index[F[_], T <: Document] {

  /**
    * Number of indexed documents
    * @return
    */
  def indexed: Int

  /**
    * Add document to index
    * @param document - data type consisting of key and sequence of search terms
    * @return
    */
  def add(document: T): F[Unit]

  /**
    * Find list of keys of documents which contains supplied search term
    * @param terms - phrase to find
    * @return
    */
  def find(terms: T#Content): F[Set[T#Key]]
}
