// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorPluginEpsonEpos",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorPluginEpsonEpos",
            targets: ["EpsonEposPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "EpsonEposPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/EpsonEposPlugin"),
        .testTarget(
            name: "EpsonEposPluginTests",
            dependencies: ["EpsonEposPlugin"],
            path: "ios/Tests/EpsonEposPluginTests")
    ]
)