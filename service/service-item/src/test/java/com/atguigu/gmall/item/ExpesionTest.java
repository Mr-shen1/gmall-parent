package com.atguigu.gmall.item;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
public class ExpesionTest {

    @Test
    public void testExpresion() {
        SpelExpressionParser parser = new SpelExpressionParser();

        String expresionStr = "sku:info:#{#args[1]}";

        Object[] args = {51, 52};

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", args);
        // 解析表达式
        Expression expression = parser.parseExpression(expresionStr, new TemplateParserContext());
        Object value = expression.getValue(context);
        System.out.println(value);

    }
}
