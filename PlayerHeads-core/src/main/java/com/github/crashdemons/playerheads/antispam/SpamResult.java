/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.antispam;

/**
 * Defines a spam-detection result object.
 * <p>
 * Used as semantic-sugar to make code self-documenting (eg:
 * `if(spampreventer.recordEvent(evt).isSpam())` is more descriptive than
 * `if(spampreventer.recordEvent(evt))`)
 * <p>
 */
public class SpamResult {

    private boolean spam;

    /**
     * <b><i>[INTERNAL - DO NOT USE]</i></b> Constructs a spam result
     *
     * @param spam the internal boolean state to abstract
     */
    public SpamResult(boolean spam) {
        this.spam = spam;
    }

    /**
     * Reports whether the spam result indicates that the record was spam or
     * not.
     * <p>
     * Reports the internal boolean state this class abstracts.
     *
     * @return true: it was detected as spam. false: it was not
     */
    public boolean isSpam() {
        return spam;
    }

    /**
     * <b><i>[INTERNAL - DO NOT USE]</i></b> Toggles the internal boolean state
     * the class abstracts.
     */
    public void toggle() {
        spam = !spam;
    }
}
