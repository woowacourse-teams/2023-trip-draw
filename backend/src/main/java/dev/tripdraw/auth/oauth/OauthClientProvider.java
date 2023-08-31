package dev.tripdraw.auth.oauth;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import dev.tripdraw.auth.domain.OauthType;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OauthClientProvider {

    private final Map<OauthType, OauthClient> mapping;

    public OauthClientProvider(Set<OauthClient> clients) {
        this.mapping = clients.stream()
                .collect(toMap(OauthClient::oauthType, identity()));
    }

    public OauthClient provide(OauthType oauthType) {
        return mapping.get(oauthType);
    }
}
