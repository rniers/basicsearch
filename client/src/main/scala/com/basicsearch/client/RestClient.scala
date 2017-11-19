package com.basicsearch.client

trait RestClient[F[_]] {

  /**
    * Add document to index
    *
    * @param key - key, document will be referenced by
    * @param document - string which contains series of terms
    */
  def put(key: String, document: String): F[Unit]

  /**
    * Get document by it's key
    * @param key - document key
    */
  def get(key: String): F[String]

  /**
    * Get list of document keys, documents of which contain
    * all of passed terms
    * @param terms - string containing search terms
    */
  def search(terms: String): F[Set[String]]
}
