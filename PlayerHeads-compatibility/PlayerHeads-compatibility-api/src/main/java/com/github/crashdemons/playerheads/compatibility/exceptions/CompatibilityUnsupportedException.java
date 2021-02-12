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
 * Unlike "Unavailable" exception, this is used for cases where support will not
 * be provided or even checked. By default this includes any server versions
 * below 1.8.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilityUnsupportedException extends CompatibilityException {

    /**
     * Constructor for unsupported-compatibility exceptions
     * @param s the string message for the exception
     * @param e the exception cause
     */
    public CompatibilityUnsupportedException(String s, Exception e) {
        super(s, e);
    }

    /**
     * Constructor for unsupported-compatibility exceptions
     * @param s the string message for the exception
     */
    public CompatibilityUnsupportedException(String s) {
        super(s);
    }
}
