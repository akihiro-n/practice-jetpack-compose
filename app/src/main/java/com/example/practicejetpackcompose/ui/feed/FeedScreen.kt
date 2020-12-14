package com.example.practicejetpackcompose.ui.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.practicejetpackcompose.ui.common.ErrorMessageScreen
import com.example.practicejetpackcompose.ui.common.ProgressScreen
import com.example.practicejetpackcompose.ui.feed.preview.FeedPreviewData
import kotlinx.coroutines.FlowPreview

private const val POSITION_NEXT_PAGE_LOADING = 3

@FlowPreview
@Composable
fun FeedScreen() {

    val viewModel = viewModel<FeedViewModel>()
    remember { viewModel.fetchFeed() }

    Scaffold(backgroundColor = Color.Black) {
        FeedItemListContent(
            items = viewModel.items,
            onClickItem = {
                when (it) {
                    is FeedItem.Article -> {
                        // TODO: 記事詳細画面へ遷移する
                    }
                    is FeedItem.Tags -> {
                        // TODO: タグの検索結果画面へ遷移する
                    }
                    else -> Unit
                }
            },
            onStartLoadNextPage = {
                /** 次ページの記事一覧をリクエストする */
                viewModel.fetchNextArticles()
            }
        )
    }
}

@Composable
fun FeedItemListContent(
    items: List<FeedItem>,
    onClickItem: (FeedItem) -> Unit,
    onStartLoadNextPage: () -> Unit
) {

    LazyColumnForIndexed(items = items) { position, item ->

        when (item) {
            is FeedItem.Progress -> ProgressScreen()
            is FeedItem.Error -> ErrorMessageScreen(item.message)
            is FeedItem.Tags -> PopularTagsContent(item)
            is FeedItem.Article -> {

                /**
                 * 次のページを読み込む
                 */
                if (position == (items.size - POSITION_NEXT_PAGE_LOADING)) {
                    onActive { onStartLoadNextPage.invoke() }
                }

                ArticleContent(item, onClickItem)
            }
            is FeedItem.Divider -> Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun PreviewFeedScreen() {
    FeedItemListContent(
        items = FeedPreviewData.tags + FeedPreviewData.articles,
        onClickItem = {},
        onStartLoadNextPage = {}
    )
}