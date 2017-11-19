package com.basicsearch.core.index

import com.basicsearch.core.model.TextDocument
import org.scalatest._

class InvertedIndexTest extends FlatSpec with Matchers {

  "InvertedIndex" should "return empty Set when empty" in {
    val idx = InvertedIndex.text

    idx.find("any term") shouldBe empty
  }

  it should "index document" in {
    val idx = InvertedIndex.text

    idx.add(TextDocument("key", "Some non empty text document"))

    idx.indexed shouldBe 1
  }

  it should "find document by terms" in {
    val idx = InvertedIndex.text

    idx.add(TextDocument("key", "Sample document"))

    val keys1: Set[String] = idx.find("Sample")
    keys1 should contain allElementsOf Set("key")

    val keys2: Set[String] = idx.find("document")
    keys2 should contain allElementsOf Set("key")

    val keys3: Set[String] = idx.find("Sample  document")
    keys3 should contain allElementsOf Set("key")
  }

  it should "be case insensitive" in {
    val idx = InvertedIndex.text

    idx.add(TextDocument("key", "Sample document"))

    val keys1: Set[String] = idx.find("sample")
    keys1 should contain allElementsOf Set("key")

    val keys2: Set[String] = idx.find("Sample")
    keys2 should contain allElementsOf Set("key")
  }

  it should "find all documents containing keys" in {
    val idx = InvertedIndex.text

    idx.add(
      TextDocument("key1", "Sample document containing some unique words"))
    idx.add(TextDocument("key2", "Sample document with other unique content"))

    val keys1: Set[String] = idx.find("some unique")
    keys1 should contain allElementsOf Set("key1")

    val keys2: Set[String] = idx.find("Sample document")
    keys2 should contain allElementsOf Set("key1", "key2")

    val keys3: Set[String] = idx.find("document sample")
    keys3 should contain allElementsOf Set("key1", "key2")

    val keys4: Set[String] = idx.find("content unique")
    keys4 should contain allElementsOf Set("key2")
  }

}
