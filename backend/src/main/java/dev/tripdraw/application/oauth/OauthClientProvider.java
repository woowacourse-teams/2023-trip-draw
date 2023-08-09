package dev.tripdraw.application.oauth;

import dev.tripdraw.domain.oauth.OauthType;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OauthClientProvider {

    private final Map<OauthType, OauthClient> mapping;

    public OauthClientProvider(Set<OauthClient> clients) {
        this.mapping = clients.stream()
                .collect(Collectors.toMap(
                        OauthClient::oauthType,
                        Function.identity()
                ));
    }

    public OauthClient provide(OauthType oauthType) {
        return mapping.get(oauthType);
    }
}
