package weclaw.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class GameCharacterForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GameCharacterForbiddenException(Long userId) {
		super("User with ID " + userId + " cannot make changes to this game character");
    }
}