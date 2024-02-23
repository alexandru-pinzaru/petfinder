package com.example.petfinder.common.data.cache

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.lang.reflect.Type
import st.lowlevel.storo.Storo

class StoroCacheManager : CacheManager {

    private val EXPIRATION_KEY = "_EXPIRATION"
    private val NO_EXPIRATION: Long = 0

    override fun <T> getListObservableUnexpired(key: String, type: Type): Observable<List<T>> {
        return Maybe.fromCallable {
            getListUnexpired<T>(key, type)
        }.toObservable()
    }

    override fun <T> getListUnexpired(key: String, type: Type): List<T>? {
        val list: List<T>? = getListEvenExpired(key, type)
        Timber.v("Get list unexpired(%s): %s", key, list?.javaClass)
        return if (hadExpired(key)) {
            Timber.v("Object not found in cache or expired: %s", key)
            null
        } else {
            list
        }
    }

    override fun <T> getListEvenExpired(key: String, type: Type): List<T>? {
        Timber.v("Get list even expired(%s)", key)
        return Storo.get<List<T>>(key, type).execute()
    }

    override fun <T> putObject(key: String, obj: T) {
        putObject(key, obj, NO_EXPIRATION)
    }

    override fun <T> putObject(key: String, obj: T, expirationMillis: Long) {
        Timber.v("putObject(%s)", key)
        var result: Boolean = Storo.put(key, obj!!).execute()
        if (!result) {
            Timber.w("Failed to store in cache(%s)", key)
        }
        if (expirationMillis != NO_EXPIRATION) {
            Timber.v("putObject with expiration(%s, %s)", key, expirationMillis)
            result = Storo.put(getExpirationKey(key), getExpirationTime(expirationMillis)).execute()
            if (!result) {
                Timber.w("Failed to store expiration in cache(%s)", key)
            }
        }
    }

    override fun <T> getObjectUnexpired(key: String, classOfT: Class<T>, defaultValue: T): T {
        return if (!hadExpired(key)) {
            getObjectEvenExpired(key, classOfT)
        } else {
            defaultValue
        }
    }

    override fun <T> getObjectEvenExpired(key: String, classOfT: Class<T>): T {
        val obj: T = Storo.get(key, classOfT).execute()
        Timber.v("Get object even expired(%s): %s", key, obj.hashCode())
        return obj
    }

    override fun deleteObject(key: String) {
        Timber.v("Remove cache for %s", key)
        Storo.delete(key)
    }

    override fun clearCache() {
        Timber.v("Clearing cache storage..")
        Storo.clear()
    }

    override fun expireKey(key: String) {
        Timber.v("expire key(%s)", key)
        val result: Boolean = Storo.put(getExpirationKey(key), System.currentTimeMillis()).execute()
        if (!result) {
            Timber.w("Failed to store expiration in cache(%s)", key)
        }
    }

    override fun containsKey(key: String): Boolean {
        return Storo.contains(key)
    }

    override fun hadExpired(key: String): Boolean {
        val expiration: Long? = Storo.get(getExpirationKey(key), Long::class.java).execute()
        return expiration != null && expiration < System.currentTimeMillis()
    }

    private fun getExpirationKey(key: String): String {
        return key + EXPIRATION_KEY
    }

    private fun getExpirationTime(expirationMillis: Long): Long {
        return if (expirationMillis == NO_EXPIRATION)
            expirationMillis else System.currentTimeMillis() + expirationMillis
    }
}