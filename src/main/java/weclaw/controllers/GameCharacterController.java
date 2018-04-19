package weclaw.controllers;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import weclaw.domain.ApplicationUser;
import weclaw.domain.GameCharacter;
import weclaw.errors.ApplicationUserNotFoundException;
import weclaw.errors.GameCharacterForbiddenException;
import weclaw.errors.GameCharacterNotFoundException;
import weclaw.repositories.ApplicationUserRepository;
import weclaw.repositories.GameCharacterRepository;

@RestController
@RequestMapping("/users/{userId}/characters")
public class GameCharacterController {

	@Autowired
	private ApplicationUserRepository userRepository;

	@Autowired
	private GameCharacterRepository characterRepository;

	@RequestMapping(method = RequestMethod.GET)
	public Collection<GameCharacter> getGameCharacters(@PathVariable Long userId) {
		this.validateUser(userId, Optional.empty());

		return this.characterRepository.findByApplicationUserId(userId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{characterId}")
	public GameCharacter getGameCharacter(@PathVariable Long userId, @PathVariable Long characterId) {
		this.validateUser(userId, Optional.empty());

		return this.characterRepository.findById(characterId)
					   				   .orElseThrow(() -> new GameCharacterNotFoundException(characterId));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/characterName/{characterName}")
	public GameCharacter getGameCharacter(@PathVariable Long userId, @PathVariable String characterName) {
		this.validateUser(userId, Optional.empty());

		return this.characterRepository.findByCharacterName(characterName)
								  	   .orElseThrow(() -> new GameCharacterNotFoundException(characterName));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postGameCharacter(Principal principal, @PathVariable Long userId, @RequestBody GameCharacter gameCharacter) {
		this.validateUser(userId, Optional.of(principal));

		return this.userRepository.findById(userId)
					   			.map(user -> {
									gameCharacter.setApplicationUser(user);
									gameCharacter.setLevel(1);
									GameCharacter result = this.characterRepository.save(gameCharacter);

									URI location = ServletUriComponentsBuilder
										.fromCurrentRequest().path("/{id}")
										.buildAndExpand(result.getId()).toUri();

									return ResponseEntity.created(location).build();
					   			})
					   			.orElseThrow(() -> new ApplicationUserNotFoundException(userId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{characterId}")
	public ResponseEntity<?> putGameCharacter(@PathVariable Long userId, @PathVariable Long characterId, 
											  @RequestBody GameCharacter gameCharacter) {
		this.validateUser(userId, Optional.empty());

		if(this.characterRepository.existsById(characterId)) {
			gameCharacter.setId(characterId);
			this.characterRepository.save(gameCharacter);

			return ResponseEntity.ok().build();
		} else {
			throw new GameCharacterNotFoundException(characterId);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{characterId}")
	public ResponseEntity<?> deleteGameCharacter(Principal principal, @PathVariable Long userId, @PathVariable Long characterId) {
		this.validateUser(userId, Optional.of(principal));

		if(this.characterRepository.existsById(characterId)) {
			this.characterRepository.deleteById(characterId);

			return ResponseEntity.ok().build();
		} else {
			throw new GameCharacterNotFoundException(characterId);
		}
	}

	private void validateUser(Long userId, Optional<Principal> principal) {
		if(!this.userRepository.existsById(userId)) {
			throw new ApplicationUserNotFoundException(userId);
		}
		if(principal.isPresent()) {
			ApplicationUser authenticatedUser = this.userRepository.findByUsername(principal.get().getName()).get();
			if(userId != authenticatedUser.getId() && !authenticatedUser.getAdmin()) {
				throw new GameCharacterForbiddenException(authenticatedUser.getId());
			}
		}
	}

}