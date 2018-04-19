package weclaw.scheduled_tasks;

import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import weclaw.domain.GameCharacter;
import weclaw.repositories.GameCharacterRepository;

@Component
public class GameCharacterScheduledTasks {

    @Autowired
    GameCharacterRepository gameCharacterRepository;

    @Scheduled(fixedRate = 5000)
    public void levelUpRandomCharacter() {
        Iterable<GameCharacter> gameCharacter = gameCharacterRepository.findAll();

        Optional<GameCharacter> lowestLevelChar = StreamSupport.stream(gameCharacter.spliterator(), true)
                                                               .min((char1, char2) -> Integer.compare(char1.getLevel(), char2.getLevel()));

        lowestLevelChar.ifPresent(character -> {
            character.setLevel(character.getLevel() + 1);
            gameCharacterRepository.save(character);   
        });
        
    }
}