/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.exceptions;

/**
 * Exception indicating that a compatibility-provider implementation isn't
 * registered at a time when it is required.
 * 
 * This occurs if you sttempt to use an implementation-specific compatibility 
 * method such as getProvider() without a provider/implementation having been 
 * made available by Compatibility.init() or registerProvider()
 * 
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilityUnregisteredException extends CompatibilityException {

    /**
     * Constructor for unregistered-compatibility exceptions
     * @param s the string message for the exception
     * @param e the exception cause
     */
    public CompatibilityUnregisteredException(String s, Exception e) {
        super(s, e);
    }
    /**
     * Constructor for unregistered-compatibility exceptions
     * @param s the string message for the exception
     */
    public CompatibilityUnregisteredException(String s) {
        super(s);
    }
}
