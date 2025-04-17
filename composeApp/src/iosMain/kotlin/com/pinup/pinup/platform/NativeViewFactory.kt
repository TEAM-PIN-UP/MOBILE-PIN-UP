package com.pinup.pinup.platform

import com.pinup.pinup.ui.map.MapViewModel
import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createNaverMap(
        viewModel: MapViewModel
    ): UIViewController
}