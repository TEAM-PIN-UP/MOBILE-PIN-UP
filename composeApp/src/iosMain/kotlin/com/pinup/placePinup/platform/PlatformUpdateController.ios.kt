package com.pinup.placePinup.platform

import com.pinup.placePinup.domain.model.UpdateStore
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual suspend fun openUpdateStore(contextFactory: ContextFactory, store: UpdateStore) {
    val url = NSURL(string = store.url)
    UIApplication.sharedApplication.openURL(url)
}
