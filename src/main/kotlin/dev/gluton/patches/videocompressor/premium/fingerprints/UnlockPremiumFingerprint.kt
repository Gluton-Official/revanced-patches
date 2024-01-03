package dev.gluton.patches.videocompressor.premium.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object UnlockPremiumFingerprint : MethodFingerprint(
    returnType = "Z",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    parameters = listOf("Landroid/content/Context;"),
    opcodes = listOf(Opcode.RETURN_VOID),
    customFingerprint = { method, classDef -> classDef.sourceFile == "BillingUtils.java" }
)
