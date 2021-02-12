/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.exceptions;

/**
 * Exception indicating that there is a conflict between multiple
 * compatibility-provider implementations.
 * This generally occurs when attempting to register a second provider for the 
 * session or initialize the compatibility library twice, mistakenly.
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilityConflictException extends CompatibilityException {

    /**
     * Constructor for conflict exceptions
     * @param s the string message for the exception
     * @param e the exception cause
     */
    public CompatibilityConflictException(String s, Exception e) {
        super(s, e);
    }

    /**
     * Constructor for conflict exceptions
     * @param s the string message for the exception
     */
    public CompatibilityConflictException(String s) {
        super(s);
    }
}
