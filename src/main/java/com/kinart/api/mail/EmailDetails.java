package com.kinart.api.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
    private String password;
    private String filename;

    public boolean isValidEmailAddress() {
        return Pattern.compile(EMAIL_PATTERN).matcher(recipient).matches();
    }
}
