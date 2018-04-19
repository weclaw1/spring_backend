package weclaw.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import weclaw.domain.ApplicationUser;
import weclaw.domain.GameCharacter;
import weclaw.repositories.ApplicationUserRepository;
import weclaw.errors.ApplicationUserNotFoundException;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationUserRepository userRepository;

	@RequestMapping(method = RequestMethod.GET)
	public Collection<ApplicationUser> getApplicationUsers() {
		return this.userRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}")
	public ApplicationUser getApplicationUser(@PathVariable Long userId) {
		return this.userRepository.findById(userId)
								  .orElseThrow(() -> new ApplicationUserNotFoundException(userId));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/username/{username}")
	public ApplicationUser getApplicationUser(@PathVariable String username) {
		return this.userRepository.findByUsername(username)
								  .orElseThrow(() -> new ApplicationUserNotFoundException(username));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postApplicationUser(@RequestBody ApplicationUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		ApplicationUser result = this.userRepository.save(user);

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/sign-up")
	public ResponseEntity<?> signUpApplicationUser(@RequestBody ApplicationUser user) {
		user.setGameCharacters(new ArrayList<GameCharacter>());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setAdmin(false);
		ApplicationUser result = this.userRepository.save(user);

		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
	public ResponseEntity<?> putApplicationUser(@PathVariable Long userId, @RequestBody ApplicationUser user) {
		if(this.userRepository.existsById(userId)) {
			user.setId(userId);
			this.userRepository.save(user);
			return ResponseEntity.ok().build();
		} else {
			throw new ApplicationUserNotFoundException(userId);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
	public ResponseEntity<?> deleteApplicationUser(@PathVariable Long userId) {
		if(this.userRepository.existsById(userId)) {
			this.userRepository.deleteById(userId);
			return ResponseEntity.ok().build();
		} else {
			throw new ApplicationUserNotFoundException(userId);
		}
	}

}