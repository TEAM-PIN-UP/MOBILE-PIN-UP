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
                isShowBookmarks: viewModel.mapUiState.isShowPinch,
                onCameraStateChange: viewModel.onCameraStateChange,
                addMarker: viewModel.addMarker,
                clearMarker: viewModel.clearMarker
            ).ignoresSafeArea(.all, edges: .top)
        }
    }
}

struct NaverMap: UIViewRepresentable {
    var position: Position?
    var cameraPosition: Position?
    var searchUiState: SearchUiState
    var isShowBookmarks: Bool
    var onCameraStateChange: (CameraState) -> Void
    var addMarker: (NMFMarker) -> Void
    var clearMarker: () -> Void
    
    func makeCoordinator() -> Coordinator {
        Coordinator(
            onCameraStateChange: onCameraStateChange
        )
    }
    
    func makeUIView(context: Context) -> NMFNaverMapView {
        // 초기 카메라 위치 설정
        let cameraPosition = NMFCameraPosition(
            NMGLatLng(lat: 37.5666102, lng: 126.9783881),
            zoom: 14.0
        )
        let view = NMFNaverMapView(frame: .zero)
        view.mapView.minZoomLevel = 5.0
        view.showCompass = false
        view.showZoomControls = false
        view.showScaleBar = false
        view.mapView.locationOverlay.hidden = false
        view.mapView.locationOverlay.icon = NMFOverlayImage(name: "ic_my_location")
        view.mapView.addCameraDelegate(delegate: context.coordinator)
        view.mapView.touchDelegate = context.coordinator
        view.mapView.moveCamera(NMFCameraUpdate(position: cameraPosition))
        return view
    }
    
    func updateUIView(_ uiView: NMFNaverMapView, context: Context) {
        print("updateUIView")
        clearMarker()
        uiView.mapView.locationOverlay.location = NMGLatLng(lat: position?.latitude ?? 0, lng: position?.longitude ?? 0)
        print("trest : \(uiView.mapView.locationOverlay.location)")
        searchUiState.reviewedPlaces.filter { reviewedPlace in
            if (isShowBookmarks) {
                reviewedPlace.bookmark
            } else {
                true
            }
        }.forEach { reviewedPlace in
            var customMarker = CustomMarker(name: reviewedPlace.name, iconName: "ic_food_marker")
            let marker = NMFMarker()
            marker.position = .init(lat: reviewedPlace.latitude, lng: reviewedPlace.longitude)
            marker.iconImage = NMFOverlayImage.init(image: customMarker.asImage())
            marker.userInfo = [
                "kakaoPlaceId" : reviewedPlace.kakaoPlaceId,
            ]
            marker.mapView = uiView.mapView
            addMarker(marker)
        }
        
        if let position = cameraPosition {
            if (position.isValid) {
                let cameraPosition = NMFCameraPosition(
                    NMGLatLng(lat: position.latitude, lng: position.longitude),
                    zoom: 14.0
                )
                let cameraUpdate = NMFCameraUpdate(position: cameraPosition)
                cameraUpdate.animation = .easeIn
                uiView.mapView.moveCamera(cameraUpdate)
            }
        }
    }
    
}

final class Coordinator: NSObject, ObservableObject, NMFMapViewCameraDelegate, NMFMapViewTouchDelegate, CLLocationManagerDelegate {
    var onCameraStateChange: (CameraState) -> Void
    
    init(onCameraStateChange: @escaping (CameraState) -> Void) {
        self.onCameraStateChange = onCameraStateChange
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
        VStack(spacing: 2) {
            Image(iconName)
            Text(name)
                .frame(maxWidth: 45)
                .lineLimit(1)
                .padding(.horizontal, 7)
                .padding(.vertical, 2)
                .font(.system(size: 11, weight: Font.Weight.medium))
                .foregroundColor(Color.white)
                .background(Color.init(UIColor(red: 0, green: 0, blue: 0, alpha: 0.25)))
                .cornerRadius(100)
        }
    }
}

extension View {
    func asImage() -> UIImage {
        let controller = UIHostingController(rootView: self)
        let view = controller.view

        let width = controller.view.intrinsicContentSize.width
        let height = UIScreen.main.scale * 45
        print("size >>> \(width) / \(height)")
        let size = CGSize(width: width, height: height)
        print("size: \(size)")
        view?.bounds = CGRect(origin: .zero, size: size)
        view?.backgroundColor = .clear

        let renderer = UIGraphicsImageRenderer(size: size)

        return renderer.image { _ in
            view?.drawHierarchy(in: controller.view.bounds, afterScreenUpdates: true)
        }
    }
}
