//
//  NaverMapViewModel.swift
//  iosApp
//
//  Created by 정상훈 on 4/7/25.
//  Updated to mirror Android MapViewModel behavior.
//

import SwiftUI
import ComposeApp
import NMapsMap

class NaverMapViewModel: ObservableObject {
    // 공유 KMP ViewModel
    private let viewModel: MapViewModel

    // Android의 uiState와 동일한 역할
    @Published var mapUiState: MapUiState

    // iOS NMaps 마커 보관(중복/누수 방지)
    private var markers: [NMFMarker] = []

    // 공통 마커 터치 핸들러 (Android onPlaceClick과 동일 동작)
    lazy var touchHandler: (NMFOverlay) -> Bool = { [weak self] overlay in
        guard
            let self,
            let kakaoPlaceId = overlay.userInfo["kakaoPlaceId"] as? String
        else { return false }
        self.onPlaceClick(kakaoPlaceId: kakaoPlaceId)
        return true
    }

    init(viewModel: MapViewModel) {
        self.viewModel = viewModel
        self.mapUiState = viewModel.uiState.value as! MapUiState

        // KMP Flow 구독 → @Published 갱신 (메인 스레드 보장)
        viewModel.uiState.collect(
            collector: Collector<MapUiState> { [weak self] state in
                guard let self else { return }
                Task { @MainActor in
                    self.mapUiState = state
                }
            },
            completionHandler: { error in
                print("uiState completed. error: \(String(describing: error?.localizedDescription))")
            }
        )
    }

    // MARK: - Android와 1:1로 대응되는 액션(패스스루)

    /// 마커 클릭 → 상세 조회
    func onPlaceClick(kakaoPlaceId: String) {
        viewModel.getDetailPlace(kakaoPlaceId: kakaoPlaceId)
    }

    /// 카메라 상태 변경 콜백
    /// Android: updateCameraState(cameraState) → getPlaces() 흐름을 동일하게 수행
    func onCameraStateChange(_ cameraState: CameraState) {
        viewModel.updateCameraState(cameraState: cameraState)
        // viewModel.getPlaces()
    }

    func updateShowPinch() {
        viewModel.updateShowPinch()
    }

    func updateFocusLocation(_ value: Bool) {
        viewModel.updateFocusLocation(value: value)
    }

    func updateSearchText(_ search: String) {
        viewModel.updateSearchText(search: search)
    }

    func updateChipState(_ chipState: ChipState) {
        viewModel.updateChipState(chipState: chipState)
    }

    func updateSortType(_ sortType: SortType) {
        viewModel.updateSortType(sortType: sortType)
    }

    func getPinchDetailList(id: Int32) {
        viewModel.getPinchDetailList(id: id)
    }

    func clearPinchDetailList() {
        viewModel.clearPinchDetailList()
    }

    func deleteReview(id: Int32) {
        viewModel.deleteReview(id: id)
    }

    func likeChanged(id: Int32, isLike: Bool) {
        viewModel.likeChanged(id: id, isLike: isLike)
    }

    func collectPosition() {
        viewModel.collectPosition()
    }

    // MARK: - iOS 네이티브(NMapsMap) 보조

    func addMarker(marker: NMFMarker) {
        marker.touchHandler = self.touchHandler
        markers.append(marker)
    }

    func clearMarker() {
        for m in markers { m.mapView = nil }
        markers.removeAll()
    }
}

// KMP Flow 수집용 Collector (그대로 사용 가능)
class Collector<T>: Kotlinx_coroutines_coreFlowCollector {
    let callback: (T) -> Void

    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        if let typed = value as? T {
            callback(typed)
        }
        completionHandler(nil)
    }
}
