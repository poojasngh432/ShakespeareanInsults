package org.radialtheater.shakespeareinsults.utilities

/**
 * Test a String to see if it starts with a vowel
 */
fun String.startsWithVowel(): Boolean {
    val firstChar = this.first().toString()
    return firstChar.matches(Regex("[aeiou]"))
}