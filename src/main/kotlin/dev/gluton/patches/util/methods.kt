package dev.gluton.patches.util

import com.android.tools.smali.dexlib2.iface.Method

val Method.signature: String get() =
    "$definingClass->$name${parameterTypes.joinToString("", prefix = "(", postfix = ")")}$returnType"
