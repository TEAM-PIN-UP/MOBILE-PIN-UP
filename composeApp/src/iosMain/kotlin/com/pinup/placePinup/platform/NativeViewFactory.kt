package com.pinup.placePinup.platform

import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.ui.map.MapViewModel
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