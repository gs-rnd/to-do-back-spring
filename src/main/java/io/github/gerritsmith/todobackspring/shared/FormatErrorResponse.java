package io.github.gerritsmith.todobackspring.shared;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class FormatErrorResponse {

    public static List<FormFieldError> springErrorsToApiResponse(Errors errors) {
        List<FormFieldError> formFieldErrors = new ArrayList<>();
        for (FieldError error : errors.getFieldErrors()) {
            formFieldErrors.add(new FormFieldError(error.getField(), error.getCode(), error.getDefaultMessage()));
        }
        return formFieldErrors;
    }

}
