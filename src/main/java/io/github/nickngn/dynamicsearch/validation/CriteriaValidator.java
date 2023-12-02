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

package io.github.nickngn.dynamicsearch.validation;

import io.github.nickngn.dynamicsearch.DSCriteria;
import io.github.nickngn.dynamicsearch.DSTemplate;
import jakarta.validation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Processor for validating syntax configured by {@link DSTemplate#getReferenceClass()}
 * and custom validation configured by {@link DSTemplate#customValidate(DSTemplate.ConditionList)}
 */
public class CriteriaValidator implements ConstraintValidator<ValidatedCriteria, DSTemplate> {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Validates the given template against custom validation rules and syntax validation rules.
     *
     * @param template The DSTemplate to be validated.
     * @param context  The ConstraintValidatorContext for custom constraint violation handling.
     * @return true if the template is valid, false otherwise.
     */
    @Override
    public boolean isValid(DSTemplate template, ConstraintValidatorContext context) {
        List<String> errMsgs = new ArrayList<>();

        errMsgs.addAll(customValidate(template));
        errMsgs.addAll(validateSyntax(template));

        if (!errMsgs.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join("; ", errMsgs))
                    .addConstraintViolation();
        }
        return errMsgs.isEmpty();
    }

    /**
     * Validates a DSTemplate using custom conditions.
     *
     * @param template The DSTemplate to be validated.
     * @return A list of error messages for conditions that evaluate to true.
     * Returns an empty list if no conditions evaluate to true or if the conditionList is null.
     */
    private static List<String> customValidate(DSTemplate template) {
        DSTemplate.ConditionList conditionList = template.customValidate(new DSTemplate.ConditionList());
        if (conditionList == null) return Collections.emptyList();

        List<String> errMsgs = new ArrayList<>();
        conditionList.getConditions().forEach(condition -> {
            if (Boolean.TRUE.equals(condition.getCondition().get())) {
                errMsgs.add(condition.getErrorMessage());
            }
        });
        return errMsgs;
    }


    /**
     * Validates the syntax of a DSTemplate.
     *
     * @param template The DSTemplate to be validated.
     * @return A list of error messages for the syntax violations found in the template.
     * Returns an empty list if no syntax violations are found or if the reference class is null.
     */
    private static List<String> validateSyntax(DSTemplate template) {
        Class<?> refClass = template.getReferenceClass();
        if (refClass == null) return Collections.emptyList();

        List<String> fields = Stream.of(refClass.getDeclaredFields()).map(Field::getName).toList();
        List<String> errMsgs = new ArrayList<>();
        for (DSCriteria DSCriteria : template.getCriteria()) {
            if (!fields.contains(DSCriteria.key())) {
                errMsgs.add(String.format("Field '%s' isn't allowed searchable key", DSCriteria.key()));
                continue;
            }
            var violations = validator.validateValue(refClass, DSCriteria.key(), DSCriteria.value());
            if (!violations.isEmpty()) {
                List<String> msgs = violations.stream().map(ConstraintViolation::getMessage).toList();
                errMsgs.addAll(msgs);
            }
        }

        return errMsgs;
    }
}
