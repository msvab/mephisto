package verify.example;

import net.svab.mephisto.jersey.ContractVerifierFilter;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import static com.google.common.io.Resources.getResource;
import static net.svab.mephisto.raml.RamlModelFactory.contractFromRaml;

public class TestUtils {

    public static String configFilePath() {
        return getResource("config.yaml").getFile();
    }

    public static JerseyClient jerseyClient() {
        JerseyClient client = JerseyClientBuilder.createClient();
        client.register(new ContractVerifierFilter(contractFromRaml("api.raml")));
        return client;
    }
}
