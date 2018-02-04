package weclaw.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApplicationUserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApplicationUserNotFoundException(String username) {
		super("Could not find user '" + username + "'.");
    }
    
    public ApplicationUserNotFoundException(Long userId) {
		super("Could not find user '" + userId + "'.");
	}
}