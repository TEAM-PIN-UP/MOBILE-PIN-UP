//
//  NaverMapViewModel.swift
//  iosApp
//
//  Created by 정상훈 on 4/7/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp
import NMapsMap

class NaverMapViewModel: ObservableObject {
    private var viewModel: MapViewModel
    @Published var mapUiState: MapUiState
    private var markers: [NMFMarker] = [NMFMarker]()
    
    init(viewModel: MapViewModel) {
        self.viewModel = viewModel
        self.mapUiState = viewModel.mapUiState.value as! MapUiState
        viewModel.mapUiState.collect(collector: Collector<MapUiState> { mapUiState in
            // do what ever you want
            print("mapUiState: \(mapUiState)")
            self.mapUiState = mapUiState
        }) { error in
            // code which is executed if the Flow object completed
            print("error: \(String(describing: error?.localizedDescription))")
        }
    }
    
    func addMarker(marker: NMFMarker) {
        markers.append(marker)
    }
    
    func clearMarker() {
        markers.forEach { NMFMarker in
            NMFMarker.mapView = nil
        }
    }
    
    func onCameraStateChange(cameraState: CameraState) {
        viewModel.getPlaces(cameraState: cameraState)
    }
}

class Collector<T> : Kotlinx_coroutines_coreFlowCollector {
    let callback:(T) -> Void

    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }
    
    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        callback(value as! T)
        completionHandler(nil)
    }
}
