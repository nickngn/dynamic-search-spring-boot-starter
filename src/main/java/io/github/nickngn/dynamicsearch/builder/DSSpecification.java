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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Convert {@link DSCriteria} content to JPA {@link Specification}
 * @param <T> searching entity
 */
@Slf4j
@SuppressWarnings("unused")
public class DSSpecification<T> implements Specification<T> {

    private final DSCriteria DSCriteria;

    public DSSpecification(DSCriteria DSCriteria) {
        this.DSCriteria = DSCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return switch (DSCriteria.operation()) {
            case CONTAINS -> onContains(root, query, cb);
            case DOES_NOT_CONTAIN -> onDoesNotContains(root, query, cb);
            case EQUAL -> onEqual(root, query, cb);
            case NOT_EQUAL -> onNotEqual(root, query, cb);
            case BEGINS_WITH -> onBeginWith(root, query, cb);
            case DOES_NOT_BEGIN_WITH -> onNotBeginWith(root, query, cb);
            case ENDS_WITH -> onEndWith(root, query, cb);
            case DOES_NOT_END_WITH -> onNotEndWith(root, query, cb);
            case NUL -> onNull(root, query, cb);
            case IN -> onIn(root, query, cb);
            case NOT_IN -> onNotIn(root, query, cb);
            case NOT_NULL -> onNotNull(root, query, cb);
            case GREATER_THAN -> onGreaterThan(root, query, cb);
            case GREATER_THAN_EQUAL -> onGreaterThanEqual(root, query, cb);
            case LESS_THAN -> onLessThan(root, query, cb);
            case LESS_THAN_EQUAL -> onLessThanEqual(root, query, cb);
        };
    }

    protected Predicate onContains(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            return cb.equal(root.get(DSCriteria.key()), null);
        }
        return cb.like(root.get(DSCriteria.key()), "%" + strVal + "%");
    }

    protected Predicate onDoesNotContains(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            return cb.notEqual(root.get(DSCriteria.key()), null);
        }
        return cb.notLike(root.get(DSCriteria.key()), "%" + strVal + "%");
    }

    protected Predicate onEqual(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get(DSCriteria.key()), strVal());
    }

    protected Predicate onNotEqual(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.notEqual(root.get(DSCriteria.key()), strVal());
    }

    protected Predicate onBeginWith(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            return cb.equal(root.get(DSCriteria.key()), null);
        }
        return cb.like(root.get(DSCriteria.key()), strVal + "%");
    }

    protected Predicate onNotBeginWith(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            return cb.notEqual(root.get(DSCriteria.key()), null);
        }
        return cb.notLike(root.get(DSCriteria.key()), strVal + "%");
    }

    protected Predicate onEndWith(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            return cb.equal(root.get(DSCriteria.key()), null);
        }
        return cb.like(root.get(DSCriteria.key()), "%" + strVal);
    }

    protected Predicate onNotEndWith(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            return cb.notEqual(root.get(DSCriteria.key()), null);
        }
        return cb.notLike(root.get(DSCriteria.key()), "%" + strVal);
    }

    protected Predicate onNull(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.isNull(root.get(DSCriteria.key()));
    }

    protected Predicate onNotNull(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.isNotNull(root.get(DSCriteria.key()));
    }

    protected Predicate onGreaterThan(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", DSCriteria.operation()));
        }
        return cb.greaterThan(root.get(DSCriteria.key()), strVal);
    }

    protected Predicate onGreaterThanEqual(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", DSCriteria.operation()));
        }
        return cb.greaterThanOrEqualTo(root.get(DSCriteria.key()), strVal);
    }

    protected Predicate onLessThan(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", DSCriteria.operation()));
        }
        return cb.lessThan(root.get(DSCriteria.key()), strVal);
    }

    protected Predicate onLessThanEqual(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strVal = strVal();
        if (strVal == null) {
            throw new InvalidCriteriaException(String.format("Value of operation '%s' can't be null", DSCriteria.operation()));
        }
        return cb.lessThanOrEqualTo(root.get(DSCriteria.key()), strVal);
    }

    protected Predicate onIn(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return root.get(DSCriteria.key()).in(arrVal());
    }

    protected Predicate onNotIn(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.not(root.get(DSCriteria.key()).in(arrVal()));
    }

    protected String strVal() {
        return DSCriteria.value() != null ? DSCriteria.value().toString() : null;
    }

    protected List<?> arrVal() {
        if (DSCriteria.value() instanceof List<?> val) {
            return val;
        }
        return List.of(DSCriteria.value());
    }
}
