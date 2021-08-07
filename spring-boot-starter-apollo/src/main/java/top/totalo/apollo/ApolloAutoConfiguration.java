package top.totalo.apollo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.totalo.apollo.core.ApolloConfigChangeProcessor;

@Configuration
@ConditionalOnProperty(prefix = "threshold.apollo", name = "enable", matchIfMissing = true)
public class ApolloAutoConfiguration {

    @Bean
    public ApolloConfigChangeProcessor apolloConfigChangeProcessor() {
        return new ApolloConfigChangeProcessor();
    }
}
