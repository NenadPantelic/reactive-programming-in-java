package sec02.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final Path ROOT_PATH = Path.of("src/main/resources/files");

    @Override
    public Mono<String> read(String filePath) {
        log.info("Reading {}", filePath);
        return Mono.fromCallable(
                () -> Files.readString(ROOT_PATH.resolve(filePath), StandardCharsets.UTF_8)
        );
    }

    @Override
    public Mono<Void> write(String filePath, String content) {
        return Mono.fromRunnable(() -> writeFile(ROOT_PATH.resolve(filePath), content));
    }

    @Override
    public Mono<Void> delete(String filePath) {
        return Mono.fromRunnable(() -> deleteFile(ROOT_PATH.resolve(filePath)));
    }

    private void writeFile(Path filePath, String content) {
        try {
            new File(filePath.toUri()).createNewFile();
            Files.writeString(filePath, content);
            log.info("Wrote content to {}", filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFile(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
            log.info("Deleted {}", filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
