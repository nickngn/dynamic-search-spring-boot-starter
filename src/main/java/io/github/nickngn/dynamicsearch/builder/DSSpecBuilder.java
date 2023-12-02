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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Builder to convert {@link List} of {@link DSCriteria} into JPA {@link Specification}
 * @param <T> searching entity
 */
public class DSSpecBuilder<T> implements ConditionalBuilder<Specification<T>> {

    @Override
    public Specification<T> build(List<DSCriteria> dsCriteriaList) {
        if (CollectionUtils.isEmpty(dsCriteriaList)) {
            return Specification.anyOf();
        }

        Specification<T> result = Specification.where(newSpec(dsCriteriaList.get(0)));
        for (int idx = 1; idx < dsCriteriaList.size(); idx++) {
            DSCriteria dsCriteria = dsCriteriaList.get(idx);
            result = dsCriteria.isOr()
                    ? Specification.where(result).or(newSpec(dsCriteria))
                    : Specification.where(result).and(
                    newSpec(dsCriteria));
        }
        return result;
    }

    private DSSpecification<T> newSpec(DSCriteria dsCriteria) {
        return new DSSpecification<>(dsCriteria);
    }
}
