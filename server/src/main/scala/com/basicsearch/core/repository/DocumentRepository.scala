package com.basicsearch.core.repository

import com.basicsearch.core.model.Document

trait DocumentRepository[F[_], T <: Document] {

  def put(document: T): F[Unit]

  def get(key: T#Key): F[Option[T]]

  def getAll(keys: Seq[T#Key]): F[Seq[T]]
}
