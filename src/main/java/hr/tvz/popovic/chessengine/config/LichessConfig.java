package hr.tvz.popovic.chessengine.config;

import chariot.Client;
import chariot.ClientAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LichessConfig {

    private final LichessProperties properties;

    @Bean
    public ClientAuth lichessClient() {
        return Client.auth(properties.getToken());
    }

}
