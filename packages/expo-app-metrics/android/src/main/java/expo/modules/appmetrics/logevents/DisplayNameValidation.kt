package expo.modules.appmetrics.logevents

import android.util.Log
import expo.modules.appmetrics.TAG

/**
 * Maximum length of a log event display name in characters. Display names are short labels
 * shown in dashboards, so they share the event-name cap rather than the longer body cap.
 * Overlong values are truncated rather than dropped so the label still renders.
 */
private const val MAX_DISPLAY_NAME_LENGTH = 256

/**
 * Suffix appended to truncated display names.
 */
private const val TRUNCATION_SUFFIX = "…"

/**
 * Validates and normalizes a caller-provided log event display name.
 *
 * Trims surrounding whitespace and returns `null` for `null` or blank input so the call site
 * can omit the field entirely. Overlong values are truncated to `MAX_DISPLAY_NAME_LENGTH`
 * characters with a warning, preserving the prefix.
 */
internal fun validateDisplayName(displayName: String?): String? {
  if (displayName == null) {
    return null
  }
  val trimmed = displayName.trim()
  if (trimmed.isEmpty()) {
    return null
  }
  if (trimmed.length <= MAX_DISPLAY_NAME_LENGTH) {
    return trimmed
  }
  val truncated = trimmed.substring(0, MAX_DISPLAY_NAME_LENGTH - TRUNCATION_SUFFIX.length) + TRUNCATION_SUFFIX
  Log.w(
    TAG,
    "[AppMetrics] logEvent truncated displayName from ${trimmed.length} characters to the $MAX_DISPLAY_NAME_LENGTH-character limit."
  )
  return truncated
}
