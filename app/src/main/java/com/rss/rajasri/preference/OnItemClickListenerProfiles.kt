package com.rss.rajasri.preference

import com.rss.rajasri.datamodels.response.ListofInvoice
import com.rss.rajasri.datamodels.response.PendingInvoicesDataModel

interface OnItemClickListenerProfiles {

    fun clickOnCurrentPositionListener(item: PendingInvoicesDataModel)

}