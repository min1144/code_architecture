package com.test.sample_architecture.api

import com.google.gson.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    private const val BASE_URL = "https://api.flickr.com"

    private fun provideOkHttpClient():
            OkHttpClient = OkHttpClient().newBuilder().run {
        addInterceptor(providerInterceptor())
        addInterceptor(provideLoggingInterceptor())
            .connectTimeout(5000, TimeUnit.SECONDS)
            .readTimeout(2000, TimeUnit.SECONDS)
    }.build()

    private fun providerInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request()
        val requeBuilder = request.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
        chain.proceed(requeBuilder.build())
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor
            = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @JvmStatic
    fun create(url: String = BASE_URL): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(url)
                .addCallAdapterFactory(ResponseFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder()))
                .client(provideOkHttpClient())
                .build()
        }.build()
    }

    private fun gsonBuilder(): Gson {
        return GsonBuilder().registerTypeAdapter(Date::class.java, GsonDateFormatAdapter()).create()
    }
}

class ResponseFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        return if (rawType == ApiCall::class.java) {
            val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
            ResponseCallAdapter<Any>(async = false, responseType = observableType)
        } else {
            ResponseCallAdapter<Any>(async = true, responseType = rawType)
        }
    }

    companion object {
        fun create(): ResponseFactory {
            return ResponseFactory()
        }
    }
}

class ResponseCallAdapter<R>(val async: Boolean, val responseType: Type) : CallAdapter<R, Any> {
    override fun adapt(call: Call<R>): Any? {
        if (async) {
            try {
                return call.execute().body()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        } else {
            return ApiCall(call)
        }
    }

    override fun responseType(): Type {
        return responseType
    }
}

class GsonDateFormatAdapter : JsonSerializer<Date?>, JsonDeserializer<Date?> {
    private val dateFormat: DateFormat

    @Synchronized
    override fun serialize(
        date: Date?,
        type: Type?,
        jsonSerializationContext: JsonSerializationContext?
    ): JsonElement {
        if (date == null) return JsonPrimitive("")
        return JsonPrimitive(dateFormat.format(date))
    }

    @Synchronized
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?
    ): Date {
        return try {
            dateFormat.parse(jsonElement.asString)!!
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }

    init {
        dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.KOREA)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
}