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

package io.github.nickngn.dynamicsearch.builder;

import io.github.nickngn.dynamicsearch.DSCriteria;

import java.util.List;

/**
 * This class is responsible for converting the content of a {@link DSCriteria} object into a SQL WHERE clause condition.
 */
public class DSPlainSqlSpec {

    private final DSCriteria dsCriteria;

    public DSPlainSqlSpec(DSCriteria dsCriteria) {
        this.dsCriteria = dsCriteria;
    }

    public String toCondition() {
        String clause = dsCriteria.isOr() ? "OR" : "AND";
        return clause + " " + generateWhereClause() ;
    }

    /**
     * This method takes the operation from the dsCriteria object and calls the respective
     * method based on the operation. The returned value is the condition separated based on
     * the operation.
     *
     * @return The condition separated based on the operation.
     */
    public String generateWhereClause() {
        return switch (dsCriteria.operation()) {
            case CONTAINS -> onContains();
            case DOES_NOT_CONTAIN -> onDoesNotContains();
            case EQUAL -> onEqual();
            case NOT_EQUAL -> onNotEqual();
            case BEGINS_WITH -> onBeginWith();
            case DOES_NOT_BEGIN_WITH -> onNotBeginWith();
            case ENDS_WITH -> onEndWith();
            case DOES_NOT_END_WITH -> onNotEndWith();
            case NUL -> onNull();
            case IN -> onIn();
            case NOT_IN -> onNotIn();
            case NOT_NULL -> onNotNull();
            case GREATER_THAN -> onGreaterThan();
            case GREATER_THAN_EQUAL -> onGreaterThanEqual();
            case LESS_THAN -> onLessThan();
            case LESS_THAN_EQUAL -> onLessThanEqual();
        };
    }

    protected String onContains() {
        String strVal = strVal();
        if (strVal == null) {
            return make("IS","NULL");
        }
        return make("LIKE", "%" + strVal + "%");
    }

    protected String onDoesNotContains() {
        String strVal = strVal();
        if (strVal == null) {
            return make("IS NOT","NULL");
        }
        return make("NOT LIKE", "%" + strVal + "%");
    }

    protected String onEqual() {
        return make("=", strVal());
    }

    protected String onNotEqual() {
        return make("<>", strVal());
    }

    protected String onBeginWith() {
        String strVal = strVal();
        if (strVal == null) {
            return make("IS", "NULL");
        }
        return make("LIKE", strVal + "%");
    }

    protected String onNotBeginWith() {
        String strVal = strVal();
        if (strVal == null) {
            return make("IS NOT", "NULL");
        }
        return make("NOT LIKE", strVal + "%");
    }

    protected String onEndWith() {
        String strVal = strVal();
        if (strVal == null) {
            return make("IS", "NULL");
        }
        return make("LIKE", "%" + strVal);
    }

    protected String onNotEndWith() {
        String strVal = strVal();
        if (strVal == null) {
            return make("IS NOT", "NULL");
        }
        return make("NOT LIKE", "%" + strVal);
    }

    protected String onNull() {
        return make("IS", "NULL");
    }

    protected String onNotNull() {
        return make("IS NOT", "NULL");
    }

    protected String onGreaterThan() {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", dsCriteria.operation()));
        }
        return make(">", strVal);
    }

    protected String onGreaterThanEqual() {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", dsCriteria.operation()));
        }
        return make(">=", strVal);
    }

    protected String onLessThan() {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", dsCriteria.operation()));
        }
        return make("<", strVal);
    }

    protected String onLessThanEqual() {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", dsCriteria.operation()));
        }
        return make("<=", strVal);
    }

    protected String onNotIn() {
        List<String> list = arrVal().stream().map(val -> "'" + val + "'").toList();
        return dsCriteria.key() + " not in " + " (" + String.join(",", list) + ")";
    }

    protected String onIn() {
        List<String> list = arrVal().stream().map(val -> "'" + val + "'").toList();
        return dsCriteria.key() + " in " + " (" + String.join(",", list) + ")";
    }

    protected String strVal() {
        return dsCriteria.value() != null ? dsCriteria.value().toString() : null;
    }

    protected List<?> arrVal() {
        if (dsCriteria.value() instanceof List<?> val) {
            return val;
        }
        return List.of(dsCriteria.value());
    }

    private String make(String operation, String value) {
        return dsCriteria.key() + " " + operation + " " + value;
    }
}
