package verify.example;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import verify.example.resource.BasicResource;
import verify.example.resource.HeadersResource;
import verify.example.resource.ParametrizedResource;
import verify.example.resource.QueryParamsResource;
import verify.example.resource.ResponseResource;
import verify.example.resource.UriParamsResource;

public class ExampleApp extends Application<Configuration> {

    @Override public void initialize(Bootstrap<Configuration> bootstrap) {

    }

    @Override public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(BasicResource.class);
        environment.jersey().register(ParametrizedResource.class);
        environment.jersey().register(UriParamsResource.class);
        environment.jersey().register(QueryParamsResource.class);
        environment.jersey().register(HeadersResource.class);
        environment.jersey().register(ResponseResource.class);
    }
}
