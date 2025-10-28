package com.pinup.pinup.platform

import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.map.MapViewModel
import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createNaverMap(
        viewModel: MapViewModel
    ): UIViewController

    fun createPintsNaverMap(
        placeList: List<Place>,
        cameraPosition: Position?,
    ) : UIViewController

    fun updatePintsNaverMap(
        controller: UIViewController,
        placeList: List<Place>,
        cameraPosition: Position?
    )
}