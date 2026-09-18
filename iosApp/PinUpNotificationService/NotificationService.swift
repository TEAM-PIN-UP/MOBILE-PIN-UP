import UserNotifications
import UniformTypeIdentifiers

final class NotificationService: UNNotificationServiceExtension {

    private var contentHandler: ((UNNotificationContent) -> Void)?
    private var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(
        _ request: UNNotificationRequest,
        withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void
      ) {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)

        defer {
          contentHandler(bestAttemptContent ?? request.content)
        }

        guard let attachment = request.attachment else { return }

        bestAttemptContent?.attachments = [attachment]
      }

      override func serviceExtensionTimeWillExpire() {
        if let contentHandler = contentHandler,
           let bestAttemptContent =  bestAttemptContent {
          contentHandler(bestAttemptContent)
        }
      }
    }

extension UNNotificationRequest {
    var attachment: UNNotificationAttachment? {
      guard
        let custom = content.userInfo["custom"] as? [String: Any],
        let attachmentURL = custom["imageUrl"] as? String,
        let url = URL(string: attachmentURL),
        let imageData = try? Data(contentsOf: url)
      else {
        return nil
      }

      return try? UNNotificationAttachment(data: imageData, options: nil)
    }
}

extension UNNotificationAttachment {
  convenience init(data: Data, options: [NSObject: AnyObject]?) throws {
    let fileManager = FileManager.default
    let temporaryFolderName = ProcessInfo.processInfo.globallyUniqueString
    let temporaryFolderURL = URL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent(temporaryFolderName, isDirectory: true)

    try fileManager.createDirectory(at: temporaryFolderURL, withIntermediateDirectories: true, attributes: nil)
    let imageFileIdentifier = UUID().uuidString + ".png"
    let fileURL = temporaryFolderURL.appendingPathComponent(imageFileIdentifier)
    try data.write(to: fileURL)
    try self.init(identifier: imageFileIdentifier, url: fileURL, options: options)
  }
}
