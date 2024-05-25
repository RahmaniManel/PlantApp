package com.example.planter_app.retrofit.plant_net_api

data class Root(
    val query: Query,
    val language: String,
    val preferedReferential: String,
    val switchToProject: String,
    val bestMatch: String,
    val results: List<Result>,
    val remainingIdentificationRequests: Long,
    val version: String,
)

data class Query(
    val project: String,
    val images: List<String>,
    val organs: List<String>,
    val includeRelatedImages: Boolean,
    val noReject: Boolean,
)

data class Result(
    val score: Double,
    val species: Species,
    val images: List<Image>,
    val gbif: Gbif,
    val powo: Powo,
    val iucn: Iucn,
)

data class Species(
    val scientificNameWithoutAuthor: String,
    val scientificNameAuthorship: String,
    val scientificName: String,
    val genus: Genus,
    val family: Family,
    val commonNames: List<String>,
)

data class Genus(
    val scientificNameWithoutAuthor: String,
    val scientificNameAuthorship: String,
    val scientificName: String,
)

data class Family(
    val scientificNameWithoutAuthor: String,
    val scientificNameAuthorship: String,
    val scientificName: String,
)

data class Image(
    val organ: String,
    val author: String,
    val license: String,
    val date: Date,
    val citation: String,
    val url: Url,
)

data class Date(
    val timestamp: Long,
    val string: String,
)

data class Url(
    val o: String,
    val m: String,
    val s: String,
)

data class Gbif(
    val id: Long,
)

data class Powo(
    val id: String,
)

data class Iucn(
    val id: String,
    val category: String,
)
