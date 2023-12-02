/*
 * MIT License
 *
 * Copyright (c) [2023] [NickNgn]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.nickngn.dynamicsearch;

import lombok.Getter;

/**
 * Support operators for searching condition
 */
@Getter
public enum DSOperation {
    CONTAINS("cn"),
    DOES_NOT_CONTAIN("nc"),
    EQUAL("eq"),
    NOT_EQUAL("ne"),
    BEGINS_WITH("bw"),
    DOES_NOT_BEGIN_WITH("bn"),
    ENDS_WITH("ew"),
    DOES_NOT_END_WITH("en"),
    NUL("nu"),
    NOT_NULL("nn"),
    GREATER_THAN("gt"),
    GREATER_THAN_EQUAL("ge"),
    LESS_THAN("lt"),
    LESS_THAN_EQUAL("le"),
    IN("in"),
    NOT_IN("ni");

    private final String value;

    DSOperation(String value) {
        this.value = value;
    }

    public static DSOperation getSimpleOperation(final String input) {
        return switch (input) {
            case "cn" -> CONTAINS;
            case "nc" -> DOES_NOT_CONTAIN;
            case "eq" -> EQUAL;
            case "ne" -> NOT_EQUAL;
            case "bw" -> BEGINS_WITH;
            case "bn" -> DOES_NOT_BEGIN_WITH;
            case "ew" -> ENDS_WITH;
            case "en" -> DOES_NOT_END_WITH;
            case "nu" -> NUL;
            case "nn" -> NOT_NULL;
            case "gt" -> GREATER_THAN;
            case "ge" -> GREATER_THAN_EQUAL;
            case "lt" -> LESS_THAN;
            case "le" -> LESS_THAN_EQUAL;
            case "in" -> IN;
            case "ni" -> NOT_IN;
            default -> null;
        };
    }
}
