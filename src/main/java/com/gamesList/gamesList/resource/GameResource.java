package com.gamesList.gamesList.resource;

import com.gamesList.gamesList.domain.Game;
import com.gamesList.gamesList.service.GamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.gamesList.gamesList.constant.Constant.photoDir;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class GameResource {
    private final GamesService gamesService;

    @PostMapping
    public ResponseEntity<Game> createContact(@RequestBody Game contact) {
        //return ResponseEntity.ok().body(contactService.createContact(contact));
        return ResponseEntity.created(URI.create("/contacts/userID")).body(gamesService.createGame(contact));
    }

    @GetMapping
    public ResponseEntity<Page<Game>> getContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(gamesService.getAllGames(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getContact(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(gamesService.getGame(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(gamesService.uploadPhoto(id, file));
    }



    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(photoDir + filename));
    }
}