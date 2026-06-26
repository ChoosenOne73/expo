// Copyright 2025-present 650 Industries. All rights reserved.

/// Maximum length of a log event display name in characters. Display names are short labels
/// shown in dashboards, so they share the event-name cap rather than the longer body cap.
/// Overlong values are truncated rather than dropped so the label still renders.
private let maxDisplayNameLength = 256

/// Suffix appended to truncated display names.
private let truncationSuffix = "…"

/// Validates and normalizes a caller-provided log event display name.
///
/// Trims surrounding whitespace and returns `nil` for `nil` or blank input so the call site
/// can omit the field entirely. Overlong values are truncated to `maxDisplayNameLength`
/// characters with a warning, preserving the prefix.
func validateDisplayName(_ displayName: String?) -> String? {
  guard let displayName else {
    return nil
  }
  let trimmed = displayName.trimmingCharacters(in: .whitespacesAndNewlines)
  if trimmed.isEmpty {
    return nil
  }
  if trimmed.count <= maxDisplayNameLength {
    return trimmed
  }
  let truncated = trimmed.prefix(maxDisplayNameLength - truncationSuffix.count) + truncationSuffix
  logger.warn(
    "[AppMetrics] logEvent truncated displayName from \(trimmed.count) characters to the \(maxDisplayNameLength)-character limit."
  )
  return String(truncated)
}
