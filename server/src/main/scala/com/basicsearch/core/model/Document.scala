package com.basicsearch.core.model

trait Document {
  type Key
  type Term
  type Content

  def key: Key
  def content: Content
}

case class TextDocument(key: String, content: String) extends Document {
  type Key = String
  type Term = String
  type Content = String
}
