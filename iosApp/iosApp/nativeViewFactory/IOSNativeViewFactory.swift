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
import KakaoSDKShare
import KakaoSDKCommon
import KakaoSDKTemplate

// MARK: - 클러스터 마커 이미지 (클러스터 개수 표시 원)
private func makeClusterImage(count: Int, size: CGFloat = 40) -> UIImage {
    let renderer = UIGraphicsImageRenderer(size: CGSize(width: size, height: size))
    return renderer.image { ctx in
        let rect = CGRect(x: 0, y: 0, width: size, height: size)
        UIColor(red: 1.0, green: 84.0 / 255.0, blue: 20.0 / 255.0, alpha: 0.85).setFill()
        ctx.cgContext.fillEllipse(in: rect)

        UIColor.white.setStroke()
        ctx.cgContext.setLineWidth(1.5)
        ctx.cgContext.strokeEllipse(in: rect.insetBy(dx: 0.75, dy: 0.75))

        let text = "\(count)" as NSString
        let font = UIFont.systemFont(ofSize: size * 0.35, weight: .bold)
        let attrs: [NSAttributedString.Key: Any] = [
            .font: font,
            .foregroundColor: UIColor.white
        ]
        let textSize = text.size(withAttributes: attrs)
        let textRect = CGRect(
            x: (size - textSize.width) / 2,
            y: (size - textSize.height) / 2,
            width: textSize.width,
            height: textSize.height
        )
        text.draw(in: textRect, withAttributes: attrs)
    }
}

// MARK: - 카테고리 그룹별 마커 이미지
// 자연(NATURE) → green, 문화생활(CULTURE) → blue, 기타(ETC) → black(red 에 tint), F&B → red
private func markerImage(for group: ComposeApp.CategoryGroup) -> UIImage? {
    switch group {
    case .fnb:
        return UIImage(named: "ic_place_circle_red")
    case .nature:
        return UIImage(named: "ic_place_circle_green")
    case .culture:
        return UIImage(named: "ic_place_circle_blue")
    case .etc:
        // black 전용 asset 이 아직 없어 tint 로 대체
        if #available(iOS 13.0, *) {
            return UIImage(named: "ic_place_circle_red")?
                .withTintColor(UIColor(red: 26/255, green: 26/255, blue: 26/255, alpha: 1.0),
                               renderingMode: .alwaysOriginal)
        }
        return UIImage(named: "ic_place_circle_red")
    default:
        return UIImage(named: "ic_place_circle_red")
    }
}

// MARK: - NMCClusteringKey 구현
// ⚠️ NMCClusteringKey 는 ObjC 프로토콜이고 NSCopying 을 포함함.
// Swift 에서 다음 두 가지가 필수:
//   1. NSCopying 을 명시적으로 채택 (그래야 copyWithZone: selector 가 정확히 매칭됨)
//   2. position 프로퍼티에 @objc 노출 (프로토콜 요구사항 매칭)
// 이게 안 맞으면 SDK 가 key 를 내부적으로 복사/해싱할 때 무시해서
// updateLeafMarker/updateClusterMarker 가 한 번도 호출되지 않음.
@objc(PlaceClusteringKey)
class PlaceClusteringKey: NSObject, NMCClusteringKey, NSCopying {
    @objc let kakaoPlaceId: String
    @objc let name: String
    @objc let isSelected: Bool
    let placeCategory: ComposeApp.Category
    @objc let position: NMGLatLng

    init(kakaoPlaceId: String, name: String, isSelected: Bool, placeCategory: ComposeApp.Category, lat: Double, lng: Double) {
        self.kakaoPlaceId = kakaoPlaceId
        self.name = name
        self.isSelected = isSelected
        self.placeCategory = placeCategory
        self.position = NMGLatLng(lat: lat, lng: lng)
        super.init()
    }

    override func isEqual(_ object: Any?) -> Bool {
        guard let other = object as? PlaceClusteringKey else { return false }
        return kakaoPlaceId == other.kakaoPlaceId
    }

    override var hash: Int { kakaoPlaceId.hashValue }

    func copy(with zone: NSZone?) -> Any {
        return PlaceClusteringKey(
            kakaoPlaceId: kakaoPlaceId,
            name: name,
            isSelected: isSelected,
            placeCategory: placeCategory,
            lat: position.lat,
            lng: position.lng
        )
    }
}

// MARK: - Leaf(단말) 마커 업데이터
// NMCLeafMarkerUpdater 프로토콜만 구현해도 SDK가 position/mapView 를 자동으로 관리하여 마커가 표시됨.
class PlaceLeafMarkerUpdater: NSObject, NMCLeafMarkerUpdater {
    weak var coordinator: NaverMap.Coordinator?

    init(coordinator: NaverMap.Coordinator) {
        self.coordinator = coordinator
    }

    func updateLeafMarker(_ info: NMCLeafMarkerInfo, _ marker: NMFMarker) {
        guard let key = info.key as? PlaceClusteringKey else {
            print("[하이] updateLeafMarker called but key cast failed: \(String(describing: info.key))")
            return
        }
        print("[하이] updateLeafMarker name=\(key.name) lat=\(key.position.lat) lng=\(key.position.lng) group=\(key.placeCategory.group) mapView=\(String(describing: marker.mapView))")

        // 카테고리 그룹별 색상 마커
        if let img = markerImage(for: key.placeCategory.group) {
            marker.iconImage = NMFOverlayImage(image: img)
        } else {
            print("[하이] updateLeafMarker markerImage(for:) returned nil for group=\(key.placeCategory.group)")
        }
        marker.width = 36
        marker.height = 36
        marker.anchor = CGPoint(x: 0.5, y: 0.5)

        // Android 와 동일한 캡션 설정
        marker.captionText = key.name
        marker.captionColor = .white
        marker.captionHaloColor = UIColor(red: 60/255, green: 60/255, blue: 60/255, alpha: 1.0)
        marker.captionTextSize = 10
        marker.captionOffset = 4
        marker.captionRequestedWidth = 240

        marker.touchHandler = { [weak self] _ in
            self?.coordinator?.onPlaceClick?(key.kakaoPlaceId)
            return true
        }
    }
}

// MARK: - Cluster 마커 업데이터
class PlaceClusterMarkerUpdater: NSObject, NMCClusterMarkerUpdater {
    func updateClusterMarker(_ info: NMCClusterMarkerInfo, _ marker: NMFMarker) {
        let count = Int(info.size)
        print("[하이] updateClusterMarker count=\(count) lat=\(info.position.lat) lng=\(info.position.lng) mapView=\(String(describing: marker.mapView))")
        marker.iconImage = NMFOverlayImage(image: makeClusterImage(count: count))
        marker.width = 40
        marker.height = 40
        marker.anchor = CGPoint(x: 0.5, y: 0.5)
        marker.captionText = ""
    }
}

// MARK: - IOSNativeViewFactory

class IOSNativeViewFactory: NativeViewFactory {
    func createNaverMap(viewModel: MapViewModel) -> UIViewController {
        let swiftUIView = MapView(viewModel: NaverMapViewModel(viewModel: viewModel))
        return UIHostingController(rootView: swiftUIView)
    }

    func createPintsNaverMap(placeList: [Place], cameraPosition: Position?) -> UIViewController {
        let swiftUIView = PintsMapView(placeList: placeList, cameraPosition: cameraPosition)
        return PintsHostingController(rootView: swiftUIView)
    }

    func updatePintsNaverMap(controller: UIViewController, placeList: [Place], cameraPosition: Position?) {
        (controller as? PintsHostingController)?
            .update(placeList: placeList, cameraPosition: cameraPosition)
    }
}

struct MapView: View {
    @ObservedObject var viewModel: NaverMapViewModel

    var body: some View {
        NaverMap(
            position: viewModel.mapUiState.currentPosition,
            cameraPosition: viewModel.mapUiState.cameraPosition,
            searchUiState: viewModel.mapUiState.searchUiState,
            pinchUiState: viewModel.mapUiState.pinchUiState,
            placeDetailUiState: viewModel.mapUiState.placeDetailUiState,
            isShowPinch: viewModel.mapUiState.isShowPinch,
            onPlaceClick: viewModel.onPlaceClick,
            onCameraStateChange: viewModel.onCameraStateChange,
            addMarker: viewModel.addMarker,
            clearMarker: viewModel.clearMarker
        ).ignoresSafeArea(.all, edges: .top)
    }
}

struct PintsMapView: View {
    var placeList: [Place]
    var cameraPosition: Position?

    var body: some View {
        PintsNaverMap(cameraPosition: cameraPosition, placeList: placeList)
            .ignoresSafeArea(.all, edges: .top)
    }
}

struct PintsNaverMap: UIViewRepresentable {
    var cameraPosition: Position?
    var placeList: [Place]

    func makeCoordinator() -> Coordinator { Coordinator() }

    func makeUIView(context: Context) -> NMFNaverMapView {
        let view = NMFNaverMapView(frame: .zero)
        view.showCompass = false
        view.showZoomControls = false
        view.showScaleBar = false
        view.mapView.minZoomLevel = 3.0
        view.mapView.locationOverlay.hidden = false
        view.mapView.locationOverlay.icon = NMFOverlayImage(name: "ic_my_location")
        let target = NMGLatLng(lat: 37.5666102, lng: 126.9783881)
        view.mapView.moveCamera(NMFCameraUpdate(position: NMFCameraPosition(target, zoom: 14.0)))
        return view
    }

    func updateUIView(_ uiView: NMFNaverMapView, context: Context) {
        let filtered = placeList.filter { !$0.kakaoPlaceId.isEmpty }

        if let pos = cameraPosition {
            uiView.mapView.moveCamera(
                NMFCameraUpdate(scrollTo: NMGLatLng(lat: pos.latitude, lng: pos.longitude))
            )
        }

        let coords = filtered.map { NMGLatLng(lat: $0.latitude, lng: $0.longitude) }
        if coords.count >= 2 {
            var minLat = coords[0].lat, maxLat = coords[0].lat
            var minLng = coords[0].lng, maxLng = coords[0].lng
            for c in coords.dropFirst() {
                if c.lat < minLat { minLat = c.lat }
                if c.lat > maxLat { maxLat = c.lat }
                if c.lng < minLng { minLng = c.lng }
                if c.lng > maxLng { maxLng = c.lng }
            }
            let bounds = NMGLatLngBounds(
                southWest: NMGLatLng(lat: minLat, lng: minLng),
                northEast: NMGLatLng(lat: maxLat, lng: maxLng)
            )
            uiView.mapView.moveCamera(NMFCameraUpdate(fit: bounds, padding: 100))
        } else if coords.count == 1 {
            uiView.mapView.moveCamera(NMFCameraUpdate(scrollTo: coords[0]))
            uiView.mapView.moveCamera(NMFCameraUpdate(zoomTo: 15.0))
        }

        if let prev = context.coordinator.polyline {
            prev.mapView = nil
            context.coordinator.polyline = nil
        }
        let pathPoints = filtered.map { NMGLatLng(lat: $0.latitude, lng: $0.longitude) }
        if pathPoints.count >= 2, let pl = NMFPolylineOverlay(pathPoints) {
            pl.width = 1
            pl.color = .systemRed
            pl.mapView = uiView.mapView
            context.coordinator.polyline = pl
        }

        context.coordinator.markers.forEach { $0.mapView = nil }
        context.coordinator.markers.removeAll()

        print("[하이] PintsNaverMap.updateUIView filtered count=\(filtered.count) mapView=\(String(describing: uiView.mapView))")
        for p in filtered {
            // ✅ MapScreen(NaverMap) 핀치 마커와 동일하게 일반 마커(원형)로 통일
            //    이름 라벨(캡슐)을 포함한 CustomMarker 를 이미지로 그린다.
            let category = ComposeApp.Category.companion.of(value: p.categoryCode)
            let iconName: String
            switch category {
            case .cafe: iconName = "ic_cafe_marker"
            default:    iconName = "ic_food_marker"
            }
            let markerView = CustomMarker(name: p.name, iconName: iconName)
            let marker = NMFMarker()
            marker.position = NMGLatLng(lat: p.latitude, lng: p.longitude)
            marker.iconImage = NMFOverlayImage(image: markerView.asImage())
            marker.anchor = CGPoint(x: 0.5, y: 0.75)
            marker.touchHandler = { _ in true }
            marker.mapView = uiView.mapView
            context.coordinator.markers.append(marker)
        }
    }

    final class Coordinator {
        var markers: [NMFMarker] = []
        var polyline: NMFPolylineOverlay?
    }
}

// MARK: - NaverMap (메인 지도 뷰, 클러스터링 지원)

struct NaverMap: UIViewRepresentable {
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

        view.showCompass = false
        view.showZoomControls = false
        view.showScaleBar = false
        view.mapView.minZoomLevel = 5.0
        view.mapView.isRotateGestureEnabled = false

        let sw = NMGLatLng(lat: 33.0, lng: 124.0)
        let ne = NMGLatLng(lat: 38.5, lng: 132.0)
        view.mapView.extent = NMGLatLngBounds(southWest: sw, northEast: ne)

        view.mapView.locationOverlay.hidden = false
        view.mapView.locationOverlay.icon = NMFOverlayImage(name: "ic_my_location")
        view.mapView.addCameraDelegate(delegate: context.coordinator)

        if let p = position, p.isValid {
            let target = NMGLatLng(lat: p.latitude, lng: p.longitude)
            view.mapView.moveCamera(NMFCameraUpdate(position: NMFCameraPosition(target, zoom: 14.0)))
        } else {
            let target = NMGLatLng(lat: 37.5666102, lng: 126.9783881)
            view.mapView.moveCamera(NMFCameraUpdate(position: NMFCameraPosition(target, zoom: 14.0)))
        }

        // ── 클러스터러 생성 ───────────────────────────────────────────────────────
        let leafUpdater = PlaceLeafMarkerUpdater(coordinator: context.coordinator)
        let clusterUpdater = PlaceClusterMarkerUpdater()

        let builder = NMCBuilder<PlaceClusteringKey>()
        builder.leafMarkerUpdater = leafUpdater
        builder.clusterMarkerUpdater = clusterUpdater
        let clusterer = builder.build()
        clusterer.mapView = view.mapView
        print("[하이] Clusterer built and attached to mapView=\(String(describing: view.mapView))")

        // ✅ leafUpdater + clusterUpdater 모두 Coordinator에 강한 참조 보관
        //    (ARC가 해제하면 클러스터링이 깨지는 버그 방지)
        context.coordinator.clusterer = clusterer
        context.coordinator.leafUpdater = leafUpdater
        context.coordinator.clusterUpdater = clusterUpdater
        // ──────────────────────────────────────────────────────────────────────────

        return view
    }

    func updateUIView(_ uiView: NMFNaverMapView, context: Context) {
        context.coordinator.onPlaceClick = onPlaceClick

        if let p = position, p.isValid {
            uiView.mapView.locationOverlay.location = NMGLatLng(lat: p.latitude, lng: p.longitude)
        }

        if let cp = cameraPosition, cp.isValid,
           context.coordinator.shouldApplyCamera(to: cp, on: uiView.mapView) {
            let pos = NMFCameraPosition(NMGLatLng(lat: cp.latitude, lng: cp.longitude), zoom: 14.0)
            let update = NMFCameraUpdate(position: pos)
            update.animation = .easeIn
            uiView.mapView.moveCamera(update)
            context.coordinator.lastAppliedCamera = cp
        }

        if isShowPinch {
            // ── 핀치 모드 ──────────────────────────────────────────────────────────
            context.coordinator.clusterer?.mapView = nil
            clearMarker()
            context.coordinator.clearPolyline()

            if !pinchUiState.editorPintsDetail.pintsPlaceList.isEmpty {
                let points = pinchUiState.editorPintsDetail.pintsPlaceList
                    .map { NMGLatLng(lat: $0.latitude, lng: $0.longitude) }
                if let pl = NMFPolylineOverlay(points) {
                    pl.width = 1
                    pl.color = UIColor.systemRed
                    pl.mapView = uiView.mapView
                    context.coordinator.polyline = pl
                }

                for p in pinchUiState.editorPintsDetail.pintsPlaceList {
                    // ✅ 일반 마커(원형)로 통일 (선택 상태 구분 없음)
                    let iconName: String
                    switch p.pintsPlaceCategory {
                    case .cafe: iconName = "ic_cafe_marker"
                    default:    iconName = "ic_food_marker"
                    }
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
        } else {
            // ── 일반 모드 (클러스터링 우회 - 직접 NMFMarker 로 표시) ─────────────────
            // NMCClusterer 가 KMP 환경의 Swift↔ObjC generic bridge 에서 leaf/cluster
            // updater 를 dispatch 하지 못하는 문제가 있어, 클러스터러를 mapView 에서
            // 떼고 직접 NMFMarker 를 그린다.
            clearMarker()
            context.coordinator.clearPolyline()

            // 클러스터러는 화면에서 분리 (간섭 방지)
            if context.coordinator.clusterer?.mapView != nil {
                context.coordinator.clusterer?.mapView = nil
            }

            let selectedPlaceId = placeDetailUiState.detailPlace?.mapPlace.kakaoPlaceId
            let places = searchUiState.reviewedPlaces

            // 데이터 변화가 없으면 스킵
            let newIds = Set(places.map { $0.kakaoPlaceId })
            guard newIds != context.coordinator.lastClusterIds
                    || selectedPlaceId != context.coordinator.lastSelectedId
            else { return }
            context.coordinator.lastClusterIds = newIds
            context.coordinator.lastSelectedId = selectedPlaceId

            // 기존 leaf 마커 제거
            context.coordinator.leafMarkers.forEach { $0.mapView = nil }
            context.coordinator.leafMarkers.removeAll()

            print("[하이] NaverMap direct-marker mode total=\(places.count)")
            for r in places {
                let category = r.placeCategory
                let m = NMFMarker()
                m.position = NMGLatLng(lat: r.latitude, lng: r.longitude)
                if let img = markerImage(for: category.group) {
                    m.iconImage = NMFOverlayImage(image: img)
                }
                // ic_my_location 의 시각적 원 크기(지름 14pt)와 비슷하게.
                // ic_place_circle_*.imageset 은 viewport 18 안에 원 지름 12 비율이라
                // 22pt 로 그리면 시각 원이 약 14pt.
                m.width = 22
                m.height = 22
                m.anchor = CGPoint(x: 0.5, y: 0.5)
                m.captionText = r.name
                m.captionColor = .white
                m.captionHaloColor = UIColor(red: 60/255, green: 60/255, blue: 60/255, alpha: 1.0)
                m.captionTextSize = 10
                m.captionOffset = 4
                m.captionRequestedWidth = 240
                let pid = r.kakaoPlaceId
                m.touchHandler = { [weak coordinator = context.coordinator] _ in
                    coordinator?.onPlaceClick?(pid)
                    return true
                }
                m.mapView = uiView.mapView
                context.coordinator.leafMarkers.append(m)
                print("[하이] NaverMap direct-marker name=\(r.name) lat=\(r.latitude) lng=\(r.longitude) group=\(category.group)")
            }
        }
    }

    private func makeMarker(name: String, iconName: String, lat: Double, lng: Double) -> NMFMarker {
        let v = CustomMarker(name: name, iconName: iconName)
        let m = NMFMarker()
        m.position = NMGLatLng(lat: lat, lng: lng)
        m.iconImage = NMFOverlayImage(image: v.asImage())
        m.anchor = CGPoint(x: 0.5, y: 0.75)
        return m
    }

    // MARK: - Coordinator

    final class Coordinator: NSObject, NMFMapViewCameraDelegate {
        var onCameraStateChange: (CameraState) -> Void
        var onPlaceClick: ((String) -> Void)?
        var polyline: NMFPolylineOverlay?
        var lastAppliedCamera: Position?

        // ✅ 클러스터러 + 두 updater 강한 참조 (ARC 해제 방지)
        var clusterer: NMCClusterer<PlaceClusteringKey>?
        var leafUpdater: PlaceLeafMarkerUpdater?
        var clusterUpdater: PlaceClusterMarkerUpdater?

        // 클러스터링 우회: 직접 NMFMarker 로 그릴 때 보관
        var leafMarkers: [NMFMarker] = []

        // 불필요한 재렌더링 방지 캐시
        var lastClusterIds: Set<String> = []
        var lastSelectedId: String? = nil

        init(onCameraStateChange: @escaping (CameraState) -> Void) {
            self.onCameraStateChange = onCameraStateChange
        }

        func clearPolyline() {
            polyline?.mapView = nil
            polyline = nil
        }

        func shouldApplyCamera(to target: Position, on mapView: NMFMapView) -> Bool {
            if let last = lastAppliedCamera,
               abs(last.latitude - target.latitude) < 1e-6,
               abs(last.longitude - target.longitude) < 1e-6 { return false }
            let cur = mapView.cameraPosition.target
            if abs(cur.lat - target.latitude) < 1e-6,
               abs(cur.lng - target.longitude) < 1e-6 { return false }
            return true
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

        func mapView(_ mapView: NMFMapView, cameraWillChangeByReason reason: Int, animated: Bool) {
            emit(mapView, moving: true, reason: reason)
        }

        func mapView(_ mapView: NMFMapView, cameraIsChangingByReason reason: Int) {
            emit(mapView, moving: true, reason: reason)
        }

        func mapView(_ mapView: NMFMapView, cameraDidChangeByReason reason: Int, animated: Bool) {
            emit(mapView, moving: true, reason: reason)
        }

        func mapViewCameraIdle(_ mapView: NMFMapView) {
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
                .frame(maxWidth: 72)
                .lineLimit(1)
                .padding(.horizontal, 6)
                .padding(.vertical, 2)
                // Android: Typography.L3(10sp) + FontWeight.W600
                .font(.system(size: 10, weight: .semibold))
                .foregroundColor(.white)
                .background(
                    Capsule()
                        .fill(Color(red: 0, green: 0, blue: 0, opacity: 0.25))
                )
                .overlay(
                    Capsule()
                        .stroke(Color(red: 60/255, green: 60/255, blue: 60/255), lineWidth: 1)
                )
        }
    }
}

extension View {
    func asImage() -> UIImage {
        let controller = UIHostingController(rootView: self)
        let view = controller.view!
        view.backgroundColor = .clear

        let size: CGSize
        if #available(iOS 16.0, *) {
            size = controller.sizeThatFits(in: CGSize(width: 200, height: 200))
        } else {
            view.frame = CGRect(origin: .zero, size: CGSize(width: 200, height: 200))
            view.setNeedsLayout()
            view.layoutIfNeeded()
            let fitted = view.systemLayoutSizeFitting(
                UIView.layoutFittingCompressedSize,
                withHorizontalFittingPriority: .fittingSizeLevel,
                verticalFittingPriority: .fittingSizeLevel
            )
            size = fitted
        }

        let validSize = CGSize(width: max(size.width, 1), height: max(size.height, 1))
        view.frame = CGRect(origin: .zero, size: validSize)
        view.setNeedsLayout()
        view.layoutIfNeeded()

        return UIGraphicsImageRenderer(size: validSize).image { _ in
            view.drawHierarchy(in: view.bounds, afterScreenUpdates: true)
        }
    }
}
