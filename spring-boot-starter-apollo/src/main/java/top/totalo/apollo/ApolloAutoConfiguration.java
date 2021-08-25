package top.totalo.apollo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.totalo.apollo.core.ApolloConfigChangeProcessor;

@Configuration
public class ApolloAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApolloConfigChangeProcessor apolloConfigChangeProcessor() {
        return new ApolloConfigChangeProcessor();
    }
}
