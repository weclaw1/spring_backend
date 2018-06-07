package weclaw.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class GameCharacter {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty(access = Access.READ_ONLY)
    private Long id;

    private String characterName;

    private Integer level;

    private String characterClass;

	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationUserId", nullable = false)
	private ApplicationUser applicationUser;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
        name = "game_character_game_item", 
        joinColumns = { @JoinColumn(name = "game_character_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "game_item_id") }
	)
	List<GameItem> gameItems;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCharacterClass() {
		return characterClass;
	}

	public void setCharacterClass(String characterClass) {
		this.characterClass = characterClass;
	}

	public ApplicationUser getApplicationUser() {
		return applicationUser;
	}

	public void setApplicationUser(ApplicationUser applicationUser) {
		this.applicationUser = applicationUser;
	}

	public String getCharacterName() {
		return characterName;
	}
	
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public List<GameItem> getGameItems() {
		return gameItems;
	}
	
	public void setGameItems(List<GameItem> gameItems) {
		this.gameItems = gameItems;
	}

}