package com.test.sample_architecture.api.datasource.base


fun Boolean?.nonnull(): Boolean {
    if (this != null) {
        return this
    }
    return false
}

fun Int?.nonnull(): Int {
    if (this != null) {
        return this
    }
    return 0
}

fun Int?.nonnull(value: Int): Int {
    if (this != null) {
        return this
    }
    return value
}

fun Float?.nonnull(): Float {
    if (this != null) {
        return this
    }
    return 0f
}

fun String?.nonnull(): String {
    if (this != null) {
        return this
    }
    return ""
}