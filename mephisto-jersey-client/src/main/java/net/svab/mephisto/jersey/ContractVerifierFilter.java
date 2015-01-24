package net.svab.mephisto.jersey;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.net.HttpHeaders;
import net.svab.mephisto.error.ContractBrokenException;
import net.svab.mephisto.model.Contract;
import net.svab.mephisto.model.ServiceHeader;
import net.svab.mephisto.model.ServiceQueryParameter;
import net.svab.mephisto.model.ServiceRequest;
import net.svab.mephisto.model.ServiceResponse;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Functions.toStringFunction;
import static com.google.common.collect.Lists.transform;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.status;

public class ContractVerifierFilter implements ClientResponseFilter, ClientRequestFilter {

    private static final String CONTRACT_BROKEN = "mephisto-contract-broken";

    private final Contract contract;

    public ContractVerifierFilter(Contract contract) {
        this.contract = contract;
    }


    @Override public void filter(ClientRequestContext request) throws IOException {
        verifyRequest(request);
    }

    @Override
    public void filter(ClientRequestContext request, ClientResponseContext response) throws IOException {
        // ignore responses where requests already broke the contract
        if (!request.getPropertyNames().contains(CONTRACT_BROKEN)) {
            verifyResponse(request, response);
        }
    }

    private void verifyRequest(ClientRequestContext request) {
        Object contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);

        ServiceRequest serviceRequest = new ServiceRequest(
                request.getUri().getPath(),
                request.getMethod(),
                contentType == null ? null : contentType.toString(),
                extractQueryParams(request),
                extractHeaders(request));

        try {
            contract.validateRequest(serviceRequest);
        } catch (ContractBrokenException cbe) {
            request.setProperty(CONTRACT_BROKEN, true);
            request.abortWith(
                    status(BAD_REQUEST)
                            .entity("Request breaks API Contract!\n" + cbe.getMessage())
                            .type(TEXT_PLAIN_TYPE)
                            .build()
            );
        }
    }

    private Set<ServiceHeader> extractHeaders(ClientRequestContext request) {
        Set<ServiceHeader> headers = new HashSet<ServiceHeader>();
        for (Map.Entry<String, List<Object>> entry : request.getHeaders().entrySet()) {
            headers.add(new ServiceHeader(entry.getKey(), transform(entry.getValue(), toStringFunction())));
        }
        return headers;
    }

    private Set<ServiceQueryParameter> extractQueryParams(ClientRequestContext request) {
        Set<ServiceQueryParameter> params = new HashSet<ServiceQueryParameter>();
        for (Map.Entry<String, Collection<String>> entry : parseQueryString(request.getUri().getQuery()).asMap().entrySet()) {
            params.add(new ServiceQueryParameter(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    private Multimap<String, String> parseQueryString(String queryString) {
        LinkedListMultimap<String, String> result = LinkedListMultimap.create();

        for (String entry : Splitter.on("&").omitEmptyStrings().split(Strings.nullToEmpty(queryString))) {
            String pair[] = entry.split("=", 2);
            try {
                result.put(URLDecoder.decode(pair[0], "UTF-8"), pair.length == 2 ? URLDecoder.decode(pair[1], "UTF-8") : null);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private void verifyResponse(ClientRequestContext request, ClientResponseContext response) {
        ServiceResponse serviceResponse = new ServiceResponse(request.getUri().getPath(), request.getMethod(), response.getStatus(), response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        try {
            contract.validateResponse(serviceResponse);
        } catch (ContractBrokenException cbe) {
            response.setStatus(INTERNAL_SERVER_ERROR.getStatusCode());
            response.getHeaders().putSingle(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN);
            response.setEntityStream(new ByteArrayInputStream(("Response breaks API Contract!\n" + cbe.getMessage()).getBytes()));
        }
    }
}
