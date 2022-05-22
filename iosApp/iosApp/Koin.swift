//
//  Koin.swift
//  iosApp
//
//  Created by Bedanta on 22/05/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared


func startKoin() {
    // You could just as easily define all these dependencies in Kotlin,
    // but this helps demonstrate how you might pass platform-specific
    // dependencies in a larger scale project where declaring them in
    // Kotlin is more difficult, or where they're also used in
    // iOS-specific code.

    let userDefaults = UserDefaults(suiteName: "DROIDCON_IN_SETTINGS")!
    let iosAppInfo = IosAppInfo()
    let doOnStartup = { NSLog("iOS app setting up...") }

    let koinApplication = KoinIosKt.doInitKoinIos(
        userDefaults: userDefaults,
        appInfo: iosAppInfo,
        doOnStartup: doOnStartup
    )
    _koin = koinApplication.koin
}

private var _koin: Koin_coreKoin?
var koin: Koin_coreKoin {
    return _koin!
}

class IosAppInfo: AppInfo {
    let appId: String = Bundle.main.bundleIdentifier!
}

