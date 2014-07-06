/*
 * Copyright 2014 the original author or authors.
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

package net.openhft.jpsg;

import java.util.Map;
import java.util.function.UnaryOperator;


public class PrimitiveTypeModifierPostProcessor extends TemplateProcessor {
    public static final int PRIORITY = OptionProcessor.PRIORITY - 1;

    private final String keyword;
    private final UnaryOperator<PrimitiveType> typeMapper;

    public PrimitiveTypeModifierPostProcessor(String keyword,
            UnaryOperator<PrimitiveType> typeMapper) {
        this.keyword = keyword;
        this.typeMapper = typeMapper;
    }

    @Override
    protected int priority() {
        return PRIORITY;
    }

    @Override
    protected void process(StringBuilder sb, Context source, Context target, String template) {
        for (Map.Entry<String, Option> e : target) {
            String dim = e.getKey();
            Option targetT = e.getValue();
            if (targetT instanceof PrimitiveType) {
                String kwDim = dim + "." + keyword;
                template = typeMapper.apply((PrimitiveType) targetT).finalReplace(template, kwDim);
            }
        }
        postProcess(sb, source, target, template);
    }
}