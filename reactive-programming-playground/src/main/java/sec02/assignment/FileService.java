package sec02.assignment;

import reactor.core.publisher.Mono;

public interface FileService {

    Mono<String> read(String filePath);

    Mono<Void> write(String filePath, String content);

    Mono<Void> delete(String filePath);
}
