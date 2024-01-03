package dev.gluton.patches.util

import app.revanced.patcher.fingerprint.MethodFingerprint
import app.revanced.patcher.fingerprint.MethodFingerprintResult
import app.revanced.patcher.patch.PatchException

@Throws(PatchException::class)
fun MethodFingerprint.resultOrThrow(): MethodFingerprintResult =
    result ?: throw PatchException("${this::class.simpleName!!} not found")
