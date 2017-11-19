package com.basicsearch.core.index

import com.basicsearch.core.model.{Document, TextDocument}

trait Tokenizer[T <: Document] {

  /**
    * Map document content into sequence of terms
    * @param content
    * @return
    */
  def tokenize(content: T#Content): Seq[T#Term]
}

class TextTokenizer extends Tokenizer[TextDocument] {

  private val stopWords = Seq()

  override def tokenize(content: String): Seq[String] =
    content
      .split(" ")
      .map(_.replaceAll("[,.!?:;]", "").trim.toLowerCase)
      .filter(_.length > 0)
      .filterNot(w => stopWords.contains(w))
}
