package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

// Extension function to fetch forecast information for given latitude and longitude
suspend fun HttpClient.getForecast(latitude: Double, longitude: Double): List<String> {
	val points = this.get("/points/$latitude,$longitude").body<Points>()
	val forecast = this.get(points.properties.forecast).body<Forecast>()
	return forecast.properties.periods.map { period ->
		"""
            ${period.name}:
            Temperature: ${period.temperature} ${period.temperatureUnit}
            Wind: ${period.windSpeed} ${period.windDirection}
            Forecast: ${period.detailedForecast}
        """.trimIndent()
	}
}

// Extension function to fetch weather alerts for a given state
suspend fun HttpClient.getAlerts(state: String): List<String> {
	val alerts = this.get("/alerts/active/area/$state").body<Alert>()
	return alerts.features.map { feature ->
		"""
            Event: ${feature.properties.event}
            Area: ${feature.properties.areaDesc}
            Severity: ${feature.properties.severity}
            Description: ${feature.properties.description}
            Instruction: ${feature.properties.instruction}
        """.trimIndent()
	}
}

@Serializable
data class Points(
	val properties: Properties
) {
	@Serializable
	data class Properties(val forecast: String)
}

@Serializable
data class Forecast(
	val properties: Properties
) {
	@Serializable
	data class Properties(val periods: List<Period>)

	@Serializable
	data class Period(
		val number: Int, val name: String, val startTime: String, val endTime: String,
		val isDaytime: Boolean, val temperature: Int, val temperatureUnit: String,
		val temperatureTrend: String, val probabilityOfPrecipitation: JsonObject,
		val windSpeed: String, val windDirection: String,
		val shortForecast: String, val detailedForecast: String,
	)
}

@Serializable
data class Alert(
	val features: List<Feature>
) {
	@Serializable
	data class Feature(
		val properties: Properties
	)

	@Serializable
	data class Properties(
		val event: String, val areaDesc: String, val severity: String,
		val description: String, val instruction: String?,
	)
}