package com.jerdouj.secureCapita.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {
    @NotEmpty
    public String email;
    @NotEmpty
    public String password;
}
