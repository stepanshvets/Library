package com.library.service.util;

import com.library.service.model.Book;
import com.library.service.model.Person;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;
        if (!Character.isUpperCase(book.getName().codePointAt(0)))
            errors.rejectValue("name", "", "Name should start with a capital letter");
        if (!Character.isUpperCase(book.getName().codePointAt(0)))
            errors.rejectValue("author", "", "Author should start with a capital letter");
    }
}
