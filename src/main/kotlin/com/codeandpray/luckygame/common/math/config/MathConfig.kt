package com.codeandpray.luckygame.common.math.config

import com.codeandpray.luckygame.common.math.PayoutCalculator
import com.codeandpray.luckygame.common.math.ProbabilityCalculator
import com.codeandpray.luckygame.common.math.random.RandomNumberGenerator
import com.codeandpray.luckygame.common.math.random.SecureRandomNumberGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class MathConfig {
    @Bean
    fun probabilityCalculator(): ProbabilityCalculator = ProbabilityCalculator()

    @Bean
    fun payoutCalculator(): PayoutCalculator = PayoutCalculator()

    @Bean
    fun randomNumberGenerator(): RandomNumberGenerator = SecureRandomNumberGenerator()
}
