package com.son.handler;

import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Collection;

@ControllerAdvice
public class DtoMapperResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType) && returnType.hasMethodAnnotation(Dto.class);
    }

    @Override
    protected void beforeBodyWriteInternal(
        MappingJacksonValue bodyContainer, MediaType contentType,
        MethodParameter returnType, ServerHttpRequest request,
        ServerHttpResponse response
    ) {
        Dto ann = returnType.getMethodAnnotation(Dto.class);

        Assert.state(ann != null, "No Dto Annotation");

        Class<?> dtoType = ann.value();
        Object value = bodyContainer.getValue();
        Object returnValue;

        if (value instanceof Collection) {
            returnValue = ((Collection<?>) value).stream().map(it -> modelMapper.map(it, dtoType));
        } else {
            returnValue = modelMapper.map(value, dtoType);
        }

        bodyContainer.setValue(returnValue);
    }
}