package com.systems.automaton.mindfullife.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.systems.automaton.mindfullife.R

private const val TAG: String = "AdManager"

class AdManager {

    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var applicationContext: Context
    var isDisabled: Boolean = false; private set
    private var isInitialized: Boolean = false

    fun initialize(context: Context) {
        if (isInitialized) {
            throw IllegalStateException("Can't initialize an already initialized singleton.")
        }

        applicationContext = context

        MobileAds.initialize(context) {}
        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR, context.getString(R.string.physical_device_id)))
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        prepareAd()
    }

    fun showAd(activity: Activity) {

        if (isDisabled) {
            return
        }

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }
    }

    private fun prepareAd() {

        if (isDisabled) {
            return
        }

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(applicationContext, applicationContext.getString(R.string.ad_unit_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")

                if (isDisabled) {
                    return
                }

                mInterstitialAd = interstitialAd

                interstitialAd.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                        prepareAd()
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.")
                        mInterstitialAd = null
                    }
                }
            }
        })
    }

    fun disableAds() {
        isDisabled = true
        mInterstitialAd = null
    }

    companion object {
        val instance = AdManager()
    }
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.showAd() = run {
    val activity = this.getActivity()
    if (activity != null) {
        AdManager.instance.showAd(activity)
    }
}