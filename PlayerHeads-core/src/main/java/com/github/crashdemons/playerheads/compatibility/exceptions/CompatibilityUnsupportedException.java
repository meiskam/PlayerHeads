/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.exceptions;

/**
 * Exception indicating a server version that is incompatible with the available
 * implementations.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilityUnsupportedException extends CompatibilityException {

    public CompatibilityUnsupportedException(String s, Exception e) {
        super(s, e);
    }

    public CompatibilityUnsupportedException(String s) {
        super(s);
    }
}
