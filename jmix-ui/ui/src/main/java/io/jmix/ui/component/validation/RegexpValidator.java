/*
 * Copyright 2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.ui.component.validation;

import io.jmix.core.Messages;
import io.jmix.core.common.util.ParamsMap;
import io.jmix.core.common.util.Preconditions;
import io.jmix.ui.component.ValidationException;
import io.jmix.ui.meta.StudioElement;
import io.jmix.ui.meta.StudioProperty;
import io.jmix.ui.substitutor.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Regexp validator checks that String value is matched with specified regular expression.
 * <p>
 * The regular expression follows the Java regular expression conventions.
 * <p>
 * For error message it uses template string and it is possible to use '${value}' key for formatted output.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in configuration class,
 * for example:
 * <pre>
 *     &#64;Bean("ui_RegexpValidator")
 *     &#64;Scope(BeanDefinition.SCOPE_PROTOTYPE)
 *     &#64;Primary
 *     protected RegexpValidator regexpValidator(String regexp) {
 *          return new CustomRegexpValidator(regexp);
 *     }
 * </pre>
 *
 * @see Pattern
 */
@StudioElement(
        caption = "RegexpValidator",
        xmlElement = "regexp",
        target = {"io.jmix.ui.component.ComboBox", "io.jmix.ui.component.TextInputField",
                "io.jmix.ui.component.SourceCodeEditor"},
        unsupportedTarget = {"io.jmix.ui.component.EntityComboBox"},
        icon = "io/jmix/ui/icon/element/validator.svg"
)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component("ui_RegexpValidator")
public class RegexpValidator extends AbstractValidator<String> {

    protected Pattern pattern;

    public RegexpValidator(String regexp) {
        Preconditions.checkNotNullArgument(regexp);

        this.pattern = Pattern.compile(regexp);
    }

    /**
     * Constructor for regexp value and custom error message. This message can contain '${value}' key for formatted output.
     * Example: "Invalid value '${value}'".
     *
     * @param regexp  regular expression
     * @param message error message
     */
    public RegexpValidator(String regexp, String message) {
        Preconditions.checkNotNullArgument(regexp);

        this.message = message;
        this.pattern = Pattern.compile(regexp);
    }

    @Autowired
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Autowired
    public void setStringSubstitutor(StringSubstitutor substitutor) {
        this.substitutor = substitutor;
    }

    /**
     * Sets regexp pattern value.
     *
     * @param regexp a regexp pattern value
     */
    @StudioProperty(required = true)
    public void setRegexp(String regexp) {
        this.pattern = Pattern.compile(regexp);
    }

    /**
     * @return a regexp pattern value
     */
    public String getRegexp() {
        return pattern.pattern();
    }

    @Override
    public void accept(String value) throws ValidationException {
        if (value == null) {
            return;
        }

        if (!pattern.matcher((value)).matches()) {
            String message = getMessage();
            if (message == null) {
                message = messages.getMessage("validation.constraints.regexp");
            }

            throw new ValidationException(getTemplateErrorMessage(message, ParamsMap.of("value", value)));
        }
    }
}
