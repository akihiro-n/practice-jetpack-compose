package com.example.practicejetpackcompose.data

import com.example.practicejetpackcompose.model.api.TagDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface TagRepository {

    enum class SORT(val value: String) {

        /**
         * 記事数順
         */
        COUNT("count"),

        /**
         * 名前順
         */
        NAME("name")
    }

    companion object {
        private const val PER_PAGE = 50L
    }

    fun getTags(
        page: Long,
        perPage: Long = PER_PAGE,
        sort: SORT = SORT.COUNT
    ): Flow<List<TagDto>>
}

class TagRepositoryImpl(private val api: QiitaApi) : TagRepository {

    override fun getTags(page: Long, perPage: Long, sort: TagRepository.SORT) =
        flow {
            val articles = api.getTags(
                page = page.toString(),
                perPage = perPage.toString(),
                sort = sort.value,
            )
            emit(articles)
        }.flowOn(Dispatchers.IO)

}