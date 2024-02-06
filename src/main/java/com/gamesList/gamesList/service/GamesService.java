package com.gamesList.gamesList.service;

import com.gamesList.gamesList.domain.Game;
import com.gamesList.gamesList.repo.GamesRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.gamesList.gamesList.constant.Constant.photoDir;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
// If a POST request doesn't go through properly, then it won't go through at all
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class GamesService {
    private final GamesRepo gamesRepo;
    public Page<Game> getAllGames(int page, int size) {
        return gamesRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Game getGame(String id) {
        return gamesRepo.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
    }
    public Game createGame(Game game) {
        return gamesRepo.save(game);
    }

    public void deleteGame(String id) {
        gamesRepo.deleteById(id);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        Game game = getGame(id);
        String photoUrl = photoFunction.apply(id, file);
        game.setPhotoUrl(photoUrl);
        gamesRepo.save(game);
        return photoUrl;
    }

    // Java 17 feats
    // (Bi)Function<(param type)..., (return type)>
    // Function which takes in a Url to a photo of String type, splits the string from the point where there's a ".".
    // It then takes everything after that index and adds a "." in front. Eg: "my/photo/url.jpg" -> ".jpg"
    private final Function<String, String> fileExtension = (fileName) -> Optional.of(fileName).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(fileName.lastIndexOf(".") + 1)).orElse(".png");
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String fileName = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(photoDir).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.
                    fromCurrentContextPath().
                    path("/games/image/" + fileName).toUriString();
        } catch(Exception exception) {
            throw new RuntimeException(("Unable to save image"));

        }
    };
}
