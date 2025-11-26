package org.kimplify.kurrency

/**
 * Shared BCP 47 language tag validation regex used by every platform implementation.
 * Format: language[-script][-region][-variant]
 */
internal val BCP47_LANGUAGE_TAG_REGEX = Regex(
    pattern = "^[a-z]{2,3}(-[A-Z][a-z]{3})?(-[A-Z]{2})?(-[0-9A-Za-z]+)*$",
    option = RegexOption.IGNORE_CASE
)

