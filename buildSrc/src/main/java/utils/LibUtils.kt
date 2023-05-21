package utils

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun lib(libName: String, version: String = ""): ReadOnlyProperty<Any, String> {
    return LibReadProperty(
        libName = libName,
        version = version,
    )
}

private class LibReadProperty(
    val libName: String,
    val version: String,
) : ReadOnlyProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        val libVersion = if (version.isNotBlank()) {
            ":$version"
        } else {
            ""
        }
        return "$libName$libVersion"
    }
}
