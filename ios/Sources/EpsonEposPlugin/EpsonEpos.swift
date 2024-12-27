import Foundation

@objc public class EpsonEpos: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
