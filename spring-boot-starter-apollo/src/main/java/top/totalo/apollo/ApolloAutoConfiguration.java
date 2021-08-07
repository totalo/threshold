package top.totalo.apollo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.totalo.apollo.core.ApolloConfigChangeProcessor;

@Configuration
public class ApolloAutoConfiguration {

    @Bean
    public ApolloConfigChangeProcessor apolloConfigChangeProcessor() {
        return new ApolloConfigChangeProcessor();
    }
}
