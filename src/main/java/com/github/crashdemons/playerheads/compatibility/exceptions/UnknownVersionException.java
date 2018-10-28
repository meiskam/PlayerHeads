/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.exceptions;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class UnknownVersionException extends IllegalStateException {
    public UnknownVersionException(String s, Exception e){ super(s,e); }
    public UnknownVersionException(String s){ super(s); }
}
