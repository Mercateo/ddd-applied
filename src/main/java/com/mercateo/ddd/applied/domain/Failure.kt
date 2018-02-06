package com.mercateo.ddd.applied.domain

class Failure<E : kotlin.Enum<E>>(vararg val causes: E)
