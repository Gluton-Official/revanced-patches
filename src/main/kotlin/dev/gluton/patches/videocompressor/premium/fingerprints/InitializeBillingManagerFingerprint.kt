package dev.gluton.patches.videocompressor.premium.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object InitializeBillingManagerFingerprint : MethodFingerprint(
    returnType = "V",
    accessFlags = AccessFlags.PUBLIC.value,
    parameters = listOf("Landroid/content/Context;"),
    customFingerprint = { method, classDef -> classDef.sourceFile == "BillingUtils.java" }
)
