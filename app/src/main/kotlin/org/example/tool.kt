package org.example

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// Create an HTTP client with a default request configuration and JSON content negotiation
val httpClient = HttpClient {
	defaultRequest {
		url("https://api.weather.gov")
		headers {
			append("Accept", "application/geo+json")
			append("User-Agent", "WeatherApiClient/1.0")
		}
		contentType(ContentType.Application.Json)
	}
	// Install content negotiation plugin for JSON serialization/deserialization
	install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
}
