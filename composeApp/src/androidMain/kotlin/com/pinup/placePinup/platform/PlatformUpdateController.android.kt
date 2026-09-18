package com.pinup.placePinup.platform

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.pinup.placePinup.domain.model.UpdateStore

actual suspend fun openUpdateStore(contextFactory: ContextFactory, store: UpdateStore) {
    val context = contextFactory.getContext() as? Context ?: return
    val intent = Intent(Intent.ACTION_VIEW, store.url.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
