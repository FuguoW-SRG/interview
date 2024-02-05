package topic.config;

import com.solacesystems.jms.SupportedProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * @ClassName JmsConfig
 * @Author yyl
 * @Date 2024-02-05 17:56:39
 * @Description JmsConfig
 * @Version 1.0
 */
@Configuration
@EnableJms
public class JmsConfig {

    private static final String SOLJMS_INITIAL_CONTEXT_FACTORY =
            "com.solacesystems.jndi.SolJNDIInitialContextFactory";

    private static String cfJNDIName = "/jms/cf/default";

    @Bean()
    public ConnectionFactory connectionFactory() throws NamingException {
        // Create to JMS Connection Factory
        Hashtable<String, Object> toEnv = new Hashtable<String, Object>();
        toEnv.put(InitialContext.INITIAL_CONTEXT_FACTORY, SOLJMS_INITIAL_CONTEXT_FACTORY);
        toEnv.put(InitialContext.PROVIDER_URL, "tcp://192.168.1.162:55556");
        toEnv.put(Context.SECURITY_PRINCIPAL, "default");
        toEnv.put(Context.SECURITY_CREDENTIALS, "default");
        toEnv.put(SupportedProperty.SOLACE_JMS_VPN, "default");

        InitialContext toInitialContext = new InitialContext(toEnv);
        return (ConnectionFactory)toInitialContext.lookup(cfJNDIName);
    }

//    @Bean(value = "jmsListenerContainerFactory")
//    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory factory,
//                                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) throws NamingException {
//        // Create to JMS Connection Factory
//        Hashtable<String, Object> toEnv = new Hashtable<String, Object>();
//        toEnv.put(InitialContext.INITIAL_CONTEXT_FACTORY, SOLJMS_INITIAL_CONTEXT_FACTORY);
//        toEnv.put(InitialContext.PROVIDER_URL, "tcp://192.168.1.162:55556");
//        toEnv.put(Context.SECURITY_PRINCIPAL, "default");
//        toEnv.put(Context.SECURITY_CREDENTIALS, "default");
//        toEnv.put(SupportedProperty.SOLACE_JMS_VPN, "default");
//
//        InitialContext toInitialContext = new InitialContext(toEnv);
//        ConnectionFactory toCF = (ConnectionFactory)toInitialContext.lookup(cfJNDIName);
//
//        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
//        configurer.configure(jmsListenerContainerFactory, toCF);
//        jmsListenerContainerFactory.setConnectionFactory(toCF);
//        return jmsListenerContainerFactory;
//    }
}
