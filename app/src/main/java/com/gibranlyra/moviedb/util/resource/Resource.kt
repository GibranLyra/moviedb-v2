package com.gibranlyra.moviedb.util.resource

open class Resource<out T> constructor(val status: ResourceState? = null,
                                       val data: T? = null, val message: String? = null,
                                       val loading: Boolean = false, val callback: (() -> Unit)? = null) {
    companion object {
        fun <T> loading(loading: Boolean): Resource<T> = Resource(ResourceState.LOADING, loading = loading)

        fun <T> success(data: T): Resource<T> = Resource(ResourceState.SUCCESS, data = data)

        fun <T> success(data: T?, message: String?): Resource<T> = Resource(ResourceState.SUCCESS, data = data,
                message = message)

        fun <T> success(): Resource<T> = Resource(ResourceState.SUCCESS)

        /**
         * Message field inside views cannot be null
         */

        fun <T> error(message: String?): Resource<T> = Resource(ResourceState.ERROR, message = message)

        fun <T> error(message: String?, callback: (() -> Unit)?): Resource<T> =
                Resource(ResourceState.ERROR, message = message, callback = callback)
    }
}
