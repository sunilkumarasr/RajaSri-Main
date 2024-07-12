package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class PendingInvoicesDataModel(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : PendingInvoicesData?   =  null
)

data class PendingInvoicesData (
    @SerializedName("pending_invoices" ) var pending_invoices : ArrayList<ListofInvoice> = arrayListOf()
)

data class ListofInvoice(
    @SerializedName("id") val id: String?,
    @SerializedName("customer_id") val customerId: String?,
    @SerializedName("city_property_id") val cityPropertyId: String?,
    @SerializedName("invoice_amount") val invoiceAmount: String?,
    @SerializedName("invoice_date") val invoiceDate: String?,
    @SerializedName("gateway_status") val gatewayStatus: String?,
    @SerializedName("payment_reference") val paymentReference: String?,
    @SerializedName("file") val file: String?,
    @SerializedName("customer_name") val customerName: String?,
    @SerializedName("property_name") val propertyName: String?
)