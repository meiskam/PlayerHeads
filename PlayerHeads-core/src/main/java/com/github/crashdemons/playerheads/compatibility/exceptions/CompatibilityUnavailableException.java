/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.exceptions;

/**
 * Exception indicating that an implementation providing compatibility for the
 * current server couldn't be found or is otherwise unavailable.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilityUnavailableException extends CompatibilityException {

    public CompatibilityUnavailableException(String s, Exception e) {
        super(s, e);
    }

    public CompatibilityUnavailableException(String s) {
        super(s);
    }
}
