package dev.gluton.patches.videocompressor.premium.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object InitializeBillingManagerFingerprint : MethodFingerprint(
    returnType = "Z",
    accessFlags = AccessFlags.PUBLIC.value,
    parameters = listOf("Landroid/content/Context;"),
    opcodes = listOf(Opcode.RETURN_VOID),
    customFingerprint = { method, classDef -> classDef.sourceFile == "BillingUtils.java" }
)
