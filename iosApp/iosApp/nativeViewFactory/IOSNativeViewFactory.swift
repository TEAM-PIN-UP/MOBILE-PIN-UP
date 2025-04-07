//
//  IOSNativeViewFactory.swift
//  iosApp
//
//  Created by 정상훈 on 4/7/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import ComposeApp
import UIKit
import SwiftUI
import NMapsMap

class IOSNativeViewFactory : NativeViewFactory {
    func createNaverMap(
        viewModel: MapViewModel
    ) -> UIViewController {
            let swiftUIView = MapView(
                viewModel: NaverMapViewModel(viewModel: viewModel)
            )
        return UIHostingController(rootView: swiftUIView)
    }
}

struct MapView: View {
    @ObservedObject var viewModel: NaverMapViewModel
    var body: some View {
        VStack {
            NaverMap(
                position: viewModel.mapUiState.currentPosition,
                cameraPosition: viewModel.mapUiState.cameraPosition,
                searchUiState: viewModel.mapUiState.searchUiState,
                onCameraStateChange: viewModel.onCameraStateChange
            ).ignoresSafeArea(.all, edges: .top)
        }
    }
}

struct NaverMap: UIViewRepresentable {
    var position: Position?
    var cameraPosition: Position?
    var searchUiState: SearchUiState
    var onCameraStateChange: (CameraState) -> Void
    
    func makeCoordinator() -> Coordinator {
        Coordinator(
            onCameraStateChange: onCameraStateChange
        )
    }
    
    func makeUIView(context: Context) -> NMFNaverMapView {
        print("position >>> \(position)")
        return context.coordinator.getNaverMapView()
    }
    
    func updateUIView(_ uiView: NMFNaverMapView, context: Context) {}
    
}

final class Coordinator: NSObject, ObservableObject, NMFMapViewCameraDelegate, NMFMapViewTouchDelegate, CLLocationManagerDelegate {
    var onCameraStateChange: (CameraState) -> Void
    init(onCameraStateChange: @escaping (CameraState) -> Void) {
        self.onCameraStateChange = onCameraStateChange
    }
    
    let view = NMFNaverMapView(frame: .zero)
    func getNaverMapView() -> NMFNaverMapView {
        view.mapView.minZoomLevel = 5.0
        view.showCompass = false
        view.showZoomControls = false
        view.showScaleBar = false
        view.mapView.addCameraDelegate(delegate: self)
        view.mapView.touchDelegate = self
        
        return view
    }
    
    // 카메라 움직임
    func mapView(_ mapView: NMFMapView, cameraDidChangeByReason reason: Int, animated: Bool) {
        onCameraStateChange(
            CameraState.init(
                isMoving: true,
                contentBounds: PositionBounds.init(
                    southWest: Position(
                        latitude: mapView.contentBounds.southWestLat,
                        longitude: mapView.contentBounds.southWestLng
                    ),
                    northEast: Position(
                        latitude: mapView.contentBounds.northEastLat,
                        longitude: mapView.contentBounds.northEastLng
                    )
                ),
                reason: getReason(reason: reason),
                position: Position(
                    latitude: mapView.cameraPosition.target.lat,
                    longitude: mapView.cameraPosition.target.lng
                )
            )
        )
    }
    
    func getReason(reason: Int) -> CameraState.Reason {
        if (reason == NMFMapChangedByGesture) {
            CameraState.Reason.gesture
        } else {
            CameraState.Reason.else_
        }
    }
    
    func mapViewCameraIdle(_ mapView: NMFMapView) {
        onCameraStateChange(
            CameraState.init(
                isMoving: false,
                contentBounds: PositionBounds.init(
                    southWest: Position(
                        latitude: mapView.contentBounds.southWestLat,
                        longitude: mapView.contentBounds.southWestLng
                    ),
                    northEast: Position(
                        latitude: mapView.contentBounds.northEastLat,
                        longitude: mapView.contentBounds.northEastLng
                    )
                ),
                reason: CameraState.Reason.else_,
                position: Position(
                    latitude: mapView.cameraPosition.target.lat,
                    longitude: mapView.cameraPosition.target.lng
                )
            )
        )
        print("idle >>> \(mapView.cameraPosition.target)")
    }
}

struct CustomMarker: View {
    var name: String
    var iconName: String
    var body: some View {
        VStack(spacing: 0) {
            Image(iconName)
            Text(name)
                .padding(.horizontal, 7)
                .padding(.vertical, 2)
                .background(Color.blue)
                .foregroundColor(.white)
                .clipShape(Capsule())
        }
    }
}
