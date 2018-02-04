package weclaw.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameCharacterNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GameCharacterNotFoundException(String characterName) {
		super("Could not find game character '" + characterName + "'.");
    }
    
	public GameCharacterNotFoundException(Long characterId) {
		super("Could not find game character '" + characterId + "'.");
    }
}