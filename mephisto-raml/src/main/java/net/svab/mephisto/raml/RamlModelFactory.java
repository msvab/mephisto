package net.svab.mephisto.raml;

import org.raml.model.Action;
import org.raml.model.MimeType;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;
import net.svab.mephisto.model.Contract;
import net.svab.mephisto.model.Header;
import net.svab.mephisto.model.ParameterType;
import net.svab.mephisto.model.QueryParameter;
import net.svab.mephisto.model.Resource;
import net.svab.mephisto.model.ResourceKey;
import net.svab.mephisto.model.Response;
import net.svab.mephisto.model.UriParameter;
import net.svab.mephisto.model.matcher.Matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static net.svab.mephisto.model.matcher.EqMatcher.eq;
import static net.svab.mephisto.model.matcher.RegexMatcher.regex;
import static net.svab.mephisto.raml.RamlValidator.validateRaml;

public class RamlModelFactory {

    public static Contract contractFromRaml(String ramlLocation) {
        validateRaml(ramlLocation);
        Raml raml = new RamlDocumentBuilder().build(ramlLocation);

        return new Contract(getResources(raml));
    }

    private static List<Resource> getResources(Raml raml) {
        List<Resource> resources = new ArrayList<Resource>();
        addResources(resources, raml.getResources().values());
        return resources;
    }

    private static void addResources(List<Resource> result, Collection<org.raml.model.Resource> resources) {
        for (org.raml.model.Resource resource : resources) {
            addResources(result, resource.getResources().values());

            if (!resource.getActions().isEmpty()) {
                Set<UriParameter> uriParameters = getUriParameters(resource);
                for (Action action : resource.getActions().values()) {
                    ResourceKey key = new ResourceKey(resource.getUri(), action.getType().name());
                    result.add(new Resource(key, uriParameters, getResponses(action.getResponses()), getRequestContentTypes(action), getQueryParams(action), getHeaders(action)));
                }
            }
        }
    }

    private static Set<Header> getHeaders(Action action) {
        Set<Header> set = new HashSet<Header>();
        for (Map.Entry<String, org.raml.model.parameter.Header> entry : action.getHeaders().entrySet()) {
            String headerName = entry.getKey();
            Matcher headerNameMatcher = headerName.contains("{?}") ? regexFromRamlWildcard(headerName) : eq(headerName);
            set.add(new Header(headerNameMatcher, ParameterType.valueOf(entry.getValue().getType().name()), entry.getValue().getEnumeration(), entry.getValue().isRequired()));
        }
        return set;
    }

    private static Matcher regexFromRamlWildcard(String headerName) {
        return regex("^" + headerName.replaceAll("\\{\\?\\}", ".*?") + "$");
    }

    private static Map<String, QueryParameter> getQueryParams(Action action) {
        Map<String, QueryParameter> params = new HashMap<String, QueryParameter>();
        for (Map.Entry<String, org.raml.model.parameter.QueryParameter> entry : action.getQueryParameters().entrySet()) {
            params.put(entry.getKey(), new QueryParameter(eq(entry.getKey()), ParameterType.valueOf(entry.getValue().getType().name()), entry.getValue().getEnumeration(), entry.getValue().isRequired()));
        }
        return params;
    }

    private static Set<String> getRequestContentTypes(Action action) {
        return action.getBody() != null ? action.getBody().keySet() : new HashSet<String>();
    }

    private static Map<Integer, Response> getResponses(Map<String, org.raml.model.Response> responses) {
        Map<Integer, Response> transformedResponses = newHashMap();
        for (Map.Entry<String, org.raml.model.Response> entry : responses.entrySet()) {
            transformedResponses.put(Integer.valueOf(entry.getKey()), new Response(getResponseContentTypes(entry.getValue().getBody())));
        }
        return transformedResponses;
    }

    private static Set<String> getResponseContentTypes(Map<String, MimeType> bodies) {
        return bodies != null ? bodies.keySet() : new HashSet<String>();
    }

    private static Set<UriParameter> getUriParameters(org.raml.model.Resource resource) {
        Set<UriParameter> uriParams = new HashSet<UriParameter>();
        for (Map.Entry<String, org.raml.model.parameter.UriParameter> entry : resource.getResolvedUriParameters().entrySet()) {
            uriParams.add(new UriParameter(entry.getKey(), ParameterType.valueOf(entry.getValue().getType().name()), entry.getValue().getEnumeration()));
        }
        return uriParams;
    }
}
