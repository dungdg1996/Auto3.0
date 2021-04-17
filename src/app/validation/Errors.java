package app.validation;

import java.util.List;
import java.util.Map;

public class Errors {

    private Map<String, String> errors;

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void reject(String item, String msgCode, String... args) {
        String msg = getMessage(msgCode, args);
        errors.put(item, msg);
    }

    public void reject(String item, String msgCode) {
        reject(item, msgCode, new String[] {});
    }

    private String getMessage(String msgCode, String[] args) {
        return "";
    }
}
