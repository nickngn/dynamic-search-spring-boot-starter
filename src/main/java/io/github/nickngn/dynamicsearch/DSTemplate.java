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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Criteria template includes comprehensive content for dynamic searching
 */
@Getter
@Setter
public abstract class DSTemplate {

    /**
     * List of specified condition on list of criteria
     */
    protected List<DSCriteria> criteria;

    /**
     * Options to retrieve the search result if there are so many
     */
    protected Pageable pageable;

    /**
     * Referred class with its fields and validation annotations to validate criteria keys with values
     */
    @JsonIgnore
    public abstract Class<?> getReferenceClass();

    /**
     * List of custom conditions for validation
     * @param conditionList initiator for adding custom conditions
     * @return list of conditions
     */
    @JsonIgnore
    public abstract ConditionList customValidate(ConditionList conditionList);

    @Getter
    @Setter
    public static class CustomCondition {
        private Supplier<Boolean> condition;
        private String errorMessage;
    }

    @Getter
    @Setter
    public static class ConditionList {
        private List<CustomCondition> conditions = new ArrayList<>();

        public ConditionList add(CustomCondition condition) {
            conditions.add(condition);
            return this;
        }
    }

    public Pageable getPageable() {
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        return pageable;
    }
}
