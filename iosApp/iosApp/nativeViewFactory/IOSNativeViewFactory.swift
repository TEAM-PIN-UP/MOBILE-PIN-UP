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
        NaverMap(
            position: viewModel.mapUiState.currentPosition,          // 현재 위치
            cameraPosition: viewModel.mapUiState.cameraPosition,     // 외부 카메라 이동 지시
            searchUiState: viewModel.mapUiState.searchUiState,       // 일반 모드 마커들
            pinchUiState: viewModel.mapUiState.pinchUiState,         // 핀치 모드 경로+마커
            placeDetailUiState: viewModel.mapUiState.placeDetailUiState,
            isShowPinch: viewModel.mapUiState.isShowPinch,
            onPlaceClick: viewModel.onPlaceClick,                    // 마커 탭 → 상세
            onCameraStateChange: viewModel.onCameraStateChange,      // 카메라 콜백
            addMarker: viewModel.addMarker,                          // 마커 보관/터치핸들러 부착
            clearMarker: viewModel.clearMarker                       // 마커 정리
        ).ignoresSafeArea(.all, edges: .top)
    }
}

// NaverMap.swift
import SwiftUI
import NMapsMap
import ComposeApp
import CoreLocation

struct NaverMap: UIViewRepresentable {
    // ⬇️ 여기! Position을 옵셔널로 변경
    var position: Position?
    var cameraPosition: Position?
    var searchUiState: SearchUiState
    var pinchUiState: PinchUiState
    var placeDetailUiState: PlaceDetailUiState
    var isShowPinch: Bool
    var onPlaceClick: (String) -> Void
    var onCameraStateChange: (CameraState) -> Void
    var addMarker: (NMFMarker) -> Void
    var clearMarker: () -> Void

    func makeCoordinator() -> Coordinator {
        Coordinator(onCameraStateChange: onCameraStateChange)
    }

    func makeUIView(context: Context) -> NMFNaverMapView {
        let view = NMFNaverMapView(frame: .zero)

        // Android MapUiSettings 대응
        view.showCompass = false
        view.showZoomControls = false
        view.showScaleBar = false
        view.mapView.minZoomLevel = 5.0
        view.mapView.isRotateGestureEnabled = false

        // 한국 영역 제한 (Android extent 와 동일)
        let sw = NMGLatLng(lat: 33.0, lng: 124.0)
        let ne = NMGLatLng(lat: 38.5, lng: 132.0)
        view.mapView.extent = NMGLatLngBounds(southWest: sw, northEast: ne)

        // 현재 위치 오버레이
        view.mapView.locationOverlay.hidden = false
        view.mapView.locationOverlay.icon = NMFOverlayImage(name: "ic_my_location")

        // 카메라 델리게이트
        view.mapView.addCameraDelegate(delegate: context.coordinator)

        // ⬇️ 초기 카메라: position이 유효하면 해당 위치, 아니면 서울 시청
        if let p = position, p.isValid {
            let target = NMGLatLng(lat: p.latitude, lng: p.longitude)
            view.mapView.moveCamera(NMFCameraUpdate(position: NMFCameraPosition(target, zoom: 14.0)))
        } else {
            let target = NMGLatLng(lat: 37.5666102, lng: 126.9783881)
            view.mapView.moveCamera(NMFCameraUpdate(position: NMFCameraPosition(target, zoom: 14.0)))
        }

        return view
    }

    func updateUIView(_ uiView: NMFNaverMapView, context: Context) {
        // ⬇️ 현재 위치 오버레이 갱신 (옵셔널 안전 언래핑)
        if let p = position, p.isValid {
            uiView.mapView.locationOverlay.location = NMGLatLng(lat: p.latitude, lng: p.longitude)
        }

        // 외부 카메라 이동 지시 (옵셔널 안전 언래핑)
        if let cp = cameraPosition, cp.isValid {
            let pos = NMFCameraPosition(NMGLatLng(lat: cp.latitude, lng: cp.longitude), zoom: 14.0)
            let update = NMFCameraUpdate(position: pos)
            update.animation = .easeIn
            uiView.mapView.moveCamera(update)
        }

        // 기존 오버레이 정리
        clearMarker()
        context.coordinator.clearPolyline()

        // 핀치 모드: 폴리라인 + 마커
        if isShowPinch, !pinchUiState.pinchDetailList.isEmpty {
            let points = pinchUiState.pinchDetailList.map { NMGLatLng(lat: $0.latitude, lng: $0.longitude) }
            let pl = NMFPolylineOverlay(points)
            pl?.width = 1
            pl?.color = UIColor.systemRed   // Colors.Negative 대체
            pl?.mapView = uiView.mapView
            context.coordinator.polyline = pl

            for p in pinchUiState.pinchDetailList {
                let selected = (p.kakaoPlaceId == placeDetailUiState.detailPlace?.mapPlace.kakaoPlaceId)
                let iconName: String = {
                    switch p.placeCategory {
                    case .restaurant: return selected ? "ic_food_marker_on" : "ic_food_marker_pinch"
                    default:          return selected ? "ic_cafe_marker_on" : "ic_cafe_marker_pinch"
                    }
                }()
                let marker = makeMarker(name: p.name, iconName: iconName, lat: p.latitude, lng: p.longitude)
                marker.userInfo = ["kakaoPlaceId": p.kakaoPlaceId]
                marker.touchHandler = { _ in
                    onPlaceClick(p.kakaoPlaceId)
                    return true
                }
                marker.mapView = uiView.mapView
                addMarker(marker)
            }
        }
        // 일반 모드: 검색 결과 마커
        else {
            for r in searchUiState.reviewedPlaces {
                let selected = (r.kakaoPlaceId == placeDetailUiState.detailPlace?.mapPlace.kakaoPlaceId)
                let iconName: String = {
                    switch r.placeCategory {
                    case .restaurant: return selected ? "ic_food_marker_on" : "ic_food_marker"
                    default:          return selected ? "ic_cafe_marker_on" : "ic_cafe_marker"
                    }
                }()
                let marker = makeMarker(name: r.name, iconName: iconName, lat: r.latitude, lng: r.longitude)
                marker.userInfo = ["kakaoPlaceId": r.kakaoPlaceId]
                marker.touchHandler = { _ in
                    onPlaceClick(r.kakaoPlaceId)
                    return true
                }
                marker.mapView = uiView.mapView
                addMarker(marker)
            }
        }
    }

    private func makeMarker(name: String, iconName: String, lat: Double, lng: Double) -> NMFMarker {
        let v = CustomMarker(name: name, iconName: iconName)
        let m = NMFMarker()
        m.position = NMGLatLng(lat: lat, lng: lng)
        m.iconImage = NMFOverlayImage(image: v.asImage())
        m.anchor = CGPoint(x: 0.5, y: 0.75) // Android Offset(0.5, 0.25)에 대응
        return m
    }

    // 카메라 콜백/폴리라인 보관
    final class Coordinator: NSObject, NMFMapViewCameraDelegate {
        var onCameraStateChange: (CameraState) -> Void
        var polyline: NMFPolylineOverlay?

        init(onCameraStateChange: @escaping (CameraState) -> Void) {
            self.onCameraStateChange = onCameraStateChange
        }

        func clearPolyline() {
            polyline?.mapView = nil
            polyline = nil
        }

        private func emit(_ mapView: NMFMapView, moving: Bool, reason: Int) {
            let b = mapView.contentBounds
            onCameraStateChange(
                CameraState(
                    isMoving: moving,
                    contentBounds: PositionBounds(
                        southWest: Position(latitude: b.southWestLat, longitude: b.southWestLng),
                        northEast: Position(latitude: b.northEastLat, longitude: b.northEastLng)
                    ),
                    reason: (reason == NMFMapChangedByGesture) ? .gesture : .else_,
                    position: Position(
                        latitude: mapView.cameraPosition.target.lat,
                        longitude: mapView.cameraPosition.target.lng
                    )
                )
            )
        }

        // 🟢 움직임 시작
        func mapView(_ mapView: NMFMapView, cameraWillChangeByReason reason: Int, animated: Bool) {
            emit(mapView, moving: true, reason: reason)
        }

        // 🟡 움직이는 중(원하면 생략해도 OK, 더 자주 업데이트됨)
        func mapView(_ mapView: NMFMapView, cameraIsChangingByReason reason: Int) {
            emit(mapView, moving: true, reason: reason)
        }

        // 🟡 변경 직후(계속 true 유지)
        func mapView(_ mapView: NMFMapView, cameraDidChangeByReason reason: Int, animated: Bool) {
            emit(mapView, moving: true, reason: reason)
        }

        // 🔴 완전히 멈춤
        func mapViewCameraIdle(_ mapView: NMFMapView) {
            // idle엔 reason 파라미터가 없으니 .else_로 보냅니다
            let b = mapView.contentBounds
            onCameraStateChange(
                CameraState(
                    isMoving: false,
                    contentBounds: PositionBounds(
                        southWest: Position(latitude: b.southWestLat, longitude: b.southWestLng),
                        northEast: Position(latitude: b.northEastLat, longitude: b.northEastLng)
                    ),
                    reason: .else_,
                    position: Position(
                        latitude: mapView.cameraPosition.target.lat,
                        longitude: mapView.cameraPosition.target.lng
                    )
                )
            )
        }
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
