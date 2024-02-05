package topic.camel;

import com.solacesystems.jms.SupportedProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import topic.pojo.CsvModel;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

/**
 * @ClassName CamelRouter
 * @Author yyl
 * @Date 2024-02-05 11:01:13
 * @Description CamelRouter
 * @Version 1.0
 */
@Component
public class CamelRouter extends RouteBuilder {

    private static final String SOLJMS_INITIAL_CONTEXT_FACTORY =
            "com.solacesystems.jndi.SolJNDIInitialContextFactory";

    private static String cfJNDIName = "/jms/cf/default";

    @Autowired
    private CamelContext camelContext;

    @Override
    public void configure() throws Exception {
        rest("/users")
                .consumes("multipart/form-data")
                .produces("application/json")

                .post().outType(CsvModel[].class)
                .responseMessage().code(200).message("All csv successfully returned").endResponseMessage()
                .to("direct:start");

        // Create to JMS Connection Factory
        Hashtable<String, Object> toEnv = new Hashtable<String, Object>();
        toEnv.put(InitialContext.INITIAL_CONTEXT_FACTORY, SOLJMS_INITIAL_CONTEXT_FACTORY);
        toEnv.put(InitialContext.PROVIDER_URL, "tcp://192.168.1.162:55556");
        toEnv.put(Context.SECURITY_PRINCIPAL, "default");
        toEnv.put(Context.SECURITY_CREDENTIALS, "default");
        toEnv.put(SupportedProperty.SOLACE_JMS_VPN, "default");

        InitialContext toInitialContext = new InitialContext(toEnv);
        ConnectionFactory toCF = (ConnectionFactory)toInitialContext.lookup(cfJNDIName);
        // Create Camel Context
        camelContext.addComponent("solace-jms", JmsComponent.jmsComponentClientAcknowledge(toCF));

        DataFormat bindy = new BindyCsvDataFormat(CsvModel.class);

        from("direct:start")
                .unmarshal(bindy)
                .marshal()
                .json(JsonLibrary.Jackson)
                .to("solace-jms:queue:orders");

    }


}
