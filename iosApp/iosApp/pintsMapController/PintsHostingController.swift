//
//  PintsHostingController.swift
//  iosApp
//
//  Created by seok on 10/28/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import ComposeApp
import SwiftUI
import UIKit
import NMapsMap

class PintsHostingController: UIHostingController<PintsMapView> {
    
    func update(placeList: [Place], cameraPosition: Position?) {
            self.rootView = PintsMapView(
                placeList: placeList,
                cameraPosition: cameraPosition
            )
    }
}
