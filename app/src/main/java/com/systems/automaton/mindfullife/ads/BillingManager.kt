package com.systems.automaton.mindfullife.ads

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import com.systems.automaton.mindfullife.R

class BillingManager {

    private lateinit var billingClient: BillingClient
    private lateinit var applicationContext: Context
    private var productDetails: ProductDetails? = null
    private var isInitialized: Boolean = false

    fun initialize(context: Context) {
        if (isInitialized) {
            throw IllegalStateException("Can't initialize an already initialized singleton.")
        }

        applicationContext = context
        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult: BillingResult, _ ->

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // TODO navigate or reload.
                }

                checkPurchases()
            }
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

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

                    checkPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun checkPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        billingClient.queryPurchasesAsync(params) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val purchaseItem = purchaseList.firstOrNull { purchase ->
                    purchase.products.contains(applicationContext.getString(R.string.ad_remove_product_name))
                }
                val unAckPurchasedItem = purchaseList.firstOrNull { purchase ->
                    !purchase.isAcknowledged
                            && purchase.products.contains(applicationContext.getString(R.string.ad_remove_product_name))
                }
                if (unAckPurchasedItem != null) {
                    val ackParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(unAckPurchasedItem.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(ackParams) { }

                    applicationContext.getActivity()?.runOnUiThread {
                        Toast.makeText(applicationContext, applicationContext.getString(R.string.thank_you_purchase), Toast.LENGTH_SHORT).show()
                    }
                }
                if (purchaseItem != null) {
                    AdManager.instance.disableAds()
                }
            } else {
                Log.d("MainActivity", "checkPurchases has billingResult of ${billingResult.responseCode} -- ${billingResult.debugMessage}")
            }
        }
    }

    fun buy() {
        productDetails?.let {
            val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(it)
                .build()
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()
            val activity = applicationContext.getActivity()
            if (activity != null) {
                billingClient.launchBillingFlow(activity, flowParams)
            }
        }
    }

    companion object {
        val instance = BillingManager()
    }
}