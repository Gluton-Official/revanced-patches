package dev.gluton.patches.videocompressor.premium

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import dev.gluton.patches.util.resultOrThrow
import dev.gluton.patches.util.signature
import dev.gluton.patches.videocompressor.premium.fingerprints.InitializeBillingManagerFingerprint
import dev.gluton.patches.videocompressor.premium.fingerprints.UnlockPremiumFingerprint

@Patch(
    name = "Unlock Premium",
    description = "Unlocks premium.",
    compatiblePackages = [
        CompatiblePackage("com.video_converter.video_compressor", ["7.1.0"])
    ],
)
@Suppress("unused")
object UnlockPremiumPatch : BytecodePatch(setOf(UnlockPremiumFingerprint, InitializeBillingManagerFingerprint)) {
    @Throws(PatchException::class)
    override fun execute(context: BytecodeContext) {
        val unlockPremiumFingerprint = UnlockPremiumFingerprint.resultOrThrow().also {
            println(it.method.signature)
        }
        InitializeBillingManagerFingerprint.resultOrThrow().let {
            println(it.method.signature)
            it.mutableMethod.apply {
                addInstruction(
                    index = implementation!!.tryBlocks.last().start.location.index,
                    "invoke-virtual {v0, v1}, ${unlockPremiumFingerprint.method.signature}".also(::println)
                )
            }
        }
    }
}
