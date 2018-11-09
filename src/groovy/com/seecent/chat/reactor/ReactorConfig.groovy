package com.seecent.chat.reactor

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.Environment
import reactor.core.Reactor
import reactor.core.spec.Reactors
import reactor.spring.context.config.EnableReactor

@Configuration
@EnableReactor
public class ReactorConfig {

    @Bean
    public Reactor reactor(Environment env) {
        return Reactors.reactor().env(env).dispatcher(Environment.THREAD_POOL).get()
    }
}
