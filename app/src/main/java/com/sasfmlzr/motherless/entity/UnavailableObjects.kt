package com.sasfmlzr.motherless.entity

object UnavailableObjects {

    val nullablePreviewData = generateSequence {  NullPreviewData()}.take(12).toList()
}