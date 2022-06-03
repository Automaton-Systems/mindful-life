package com.systems.automaton.mindfullife.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavHostController
import com.android.billingclient.api.*
import com.systems.automaton.mindfullife.BuildConfig
import com.systems.automaton.mindfullife.R
import com.systems.automaton.mindfullife.presentation.util.Screen

class BillingManager {

    private lateinit var billingClient: BillingClient
    private var productDetails: ProductDetails? = null
    private var isInitialized: Boolean = false
    var navController: NavHostController? = null

    fun initialize(context: Context) {
        if (isInitialized) {
            throw IllegalStateException("Can't initialize an already initialized singleton.")
        }

        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult: BillingResult, _ ->

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    navController?.let {
                        it.navigate(Screen.SettingsScreen.route)
                    }
                }

                checkPurchases(context)
            }
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
                    && productDetails == null) {

                    val productList = listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(context.getString(R.string.ad_remove_product_name))
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build())
                    val params = QueryProductDetailsParams.newBuilder()
                        .setProductList(productList)
                        .build()
                    billingClient.queryProductDetailsAsync(params) { queryProductBillingResult, productDetailsList ->
                        if (queryProductBillingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            productDetails = productDetailsList.firstOrNull()
                        }
                    }

                    checkPurchases(context)
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun checkPurchases(context: Context) {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        billingClient.queryPurchasesAsync(params) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val purchaseItem = purchaseList.firstOrNull { purchase ->
                    purchase.products.contains(context.getString(R.string.ad_remove_product_name))
                }
                val unAckPurchasedItem = purchaseList.firstOrNull { purchase ->
                    !purchase.isAcknowledged
                            && purchase.products.contains(context.getString(R.string.ad_remove_product_name))
                }
                if (unAckPurchasedItem != null) {
                    val ackParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(unAckPurchasedItem.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(ackParams) { }

                    context.getActivity()?.runOnUiThread {
                        Toast.makeText(context, context.getString(R.string.thank_you_purchase), Toast.LENGTH_SHORT).show()
                    }
                }
                if (purchaseItem != null) {
                    AdManager.instance.disableAds()

                    // TESTING: consume the purchase if we refunded.
                    //testingConsumePurchase(purchaseItem)
                }
            } else {
                Log.d("BillingManager", "checkPurchases has billingResult of ${billingResult.responseCode} -- ${billingResult.debugMessage}")
            }
        }
    }

    fun buy(activity: Activity) {
        productDetails?.let {
            val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(it)
                .build()
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()
            billingClient.launchBillingFlow(activity, flowParams)
        }
    }

    fun testingConsumePurchase(purchaseItem: Purchase?) {

        if (!BuildConfig.DEBUG) {
            return
        }

        purchaseItem?.let {
            val params = ConsumeParams.newBuilder()
                .setPurchaseToken(it.purchaseToken)
                .build()
            billingClient.consumeAsync(params) { billingResult, purchaseToken ->
                Log.d("BillingManager", "consumedPurchase -- $billingResult $purchaseToken")
            }
        }
    }

    companion object {
        val instance = BillingManager()
    }
}