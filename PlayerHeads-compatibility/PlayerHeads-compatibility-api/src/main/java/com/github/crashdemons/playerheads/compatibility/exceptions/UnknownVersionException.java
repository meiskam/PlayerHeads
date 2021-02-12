/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.exceptions;

/**
 * Exception indicating a server version that could not be retrieved or
 * understood.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class UnknownVersionException extends VersionException {

    /**
     * Constructor for unknown-version exceptions
     * @param s the string message for the exception
     * @param e the exception cause
     */
    public UnknownVersionException(String s, Exception e) {
        super(s, e);
    }
    /**
     * Constructor for unknown-version exceptions
     * @param s the string message for the exception
     */
    public UnknownVersionException(String s) {
        super(s);
    }
}
