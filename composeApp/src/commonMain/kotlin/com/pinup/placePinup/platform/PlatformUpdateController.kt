package com.pinup.placePinup.platform

import com.pinup.placePinup.domain.model.UpdateStore

expect suspend fun openUpdateStore(contextFactory: ContextFactory, store: UpdateStore)
