package dev.gluton.patches.videocompressor.premium.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object UnlockPremiumFingerprint : MethodFingerprint(
    returnType = "V",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    parameters = listOf("Landroid/content/Context;"),
    customFingerprint = { method, classDef -> classDef.sourceFile == "BillingUtils.java" }
)
