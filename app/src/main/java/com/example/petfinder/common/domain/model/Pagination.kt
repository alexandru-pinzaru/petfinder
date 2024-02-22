package com.example.petfinder.common.domain.model

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("count_per_page") val countPerPage: Int?,
    @SerializedName("total_count") val totalCount: Int?,
    @SerializedName("current_page") val currentPage: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("_links") val links: PaginationLinks,
)

data class PaginationLinks(
    @SerializedName("previous") val previous: PaginationLink,
    @SerializedName("next") val next: PaginationLink,
)

data class PaginationLink(
    @SerializedName("href") val href: String,
)