package com.purenative.system

enum class OperatingSystem {
    ANDROID, IOS;

    companion object { }
}

expect fun OperatingSystem.Companion.current(): OperatingSystem