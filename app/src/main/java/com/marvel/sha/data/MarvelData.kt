package com.marvel.sha.data

internal data class MarvelData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Map<String, Any>>,
) {
    fun isLastPage() = count == 0 || (offset + count) == total
}
