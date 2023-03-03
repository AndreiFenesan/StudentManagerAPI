package com.example.demo.validators;

public interface Validator<T> {
    public void validate(T entity);
}
