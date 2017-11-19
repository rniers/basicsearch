package com.basicsearch.core.service

import cats._
import cats.syntax.all._
import com.basicsearch.client.RestClient
import com.basicsearch.core.index.{Index, InvertedIndex}
import com.basicsearch.core.model.{Document, TextDocument}
import com.basicsearch.core.repository.{
  DocumentRepository,
  InMemoryDocumentRepository
}
import com.twitter.util.Future

import scala.languageFeature.higherKinds

trait SearchService[F[_], T <: Document] {

  /**
    *
    * @param document
    * @return
    */
  def put(document: T): F[Unit]

  /**
    *
    * @param key
    * @return
    */
  def get(key: T#Key): F[Option[T]]

  /**
    * Find list of keys of documents which contains supplied search term
    * @return
    */
  def search(terms: T#Content): F[Set[T#Key]]
}

class SearchServiceImpl[F[_]: Monad, G[_]: Monad, T <: Document](
    repository: DocumentRepository[F, T],
    index: Index[G, T]
)(implicit GF: G ~> F)
    extends SearchService[F, T] {

  override def put(document: T): F[Unit] =
    GF(index.add(document)).flatMap(_ => repository.put(document))

  override def get(key: T#Key): F[Option[T]] =
    repository.get(key)

  override def search(query: T#Content): F[Set[T#Key]] = GF {
    index.find(query)
  }
}

/**
  * Stores documents in memory
  */
class LocalSearchService extends SearchService[Future, TextDocument] {

  import SearchService._

  private val service = new SearchServiceImpl[Id, Id, TextDocument](
    InMemoryDocumentRepository.apply,
    InvertedIndex.text)

  override def put(document: TextDocument): Future[Unit] =
    Future.value(service.put(document))

  override def get(key: String) =
    Future.value(service.get(key))

  override def search(terms: String) =
    Future.value(service.search(terms))

}

/**
  * Store documents and index on remote shards
  * @param clients - list of rest clients
  */
class RemoteSearchService(clients: Seq[RestClient[Future]])
    extends SearchService[Future, TextDocument] {

  def shard(key: String): RestClient[Future] =
    clients(key.hashCode % clients.length)

  override def put(document: TextDocument): Future[Unit] =
    shard(document.key).put(document.key, document.content)

  override def get(key: String): Future[Option[TextDocument]] =
    shard(key)
      .get(key)
      .map(text => Some(TextDocument(key, text)))
      .handle {
        case _: Throwable => None
      }

  override def search(terms: String): Future[Set[String]] =
    Future
      .collect(clients.map(_.search(terms)))
      .map(_.foldLeft(Set.empty[String])(_ ++ _))
}

object SearchService {
  implicit val idToId: Id ~> Id = new ~>[Id, Id] {
    override def apply[A](fa: Id[A]): Id[A] = fa
  }
}
