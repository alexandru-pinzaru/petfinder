package com.example.petfinder.common.data.cache

import io.reactivex.rxjava3.core.Observable
import java.lang.reflect.Type

interface CacheManager {

    fun <T> getListObservableUnexpired(key: String, type: Type): Observable<List<T>>

    fun <T> getListUnexpired(key: String, type: Type): List<T>?

    fun <T> getListEvenExpired(key: String, type: Type): List<T>?

    fun <T> putObject(key: String, obj: T)

    fun <T> putObject(key: String, obj: T, expirationMillis: Long)

    fun <T> getObjectUnexpired(key: String, classOfT: Class<T>, defaultValue: T): T

    fun <T> getObjectEvenExpired(key: String, classOfT: Class<T>): T

    fun deleteObject(key: String)

    fun clearCache()

    fun expireKey(key: String)

    fun containsKey(key: String): Boolean

}