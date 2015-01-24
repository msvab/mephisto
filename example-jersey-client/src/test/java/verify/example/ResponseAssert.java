package verify.example;

import com.google.common.io.CharStreams;
import net.svab.mephisto.error.ContractBrokenException;
import org.assertj.core.api.AbstractAssert;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

    public ResponseAssert(Response response) {
        super(response, ResponseAssert.class);
    }

    public static ResponseAssert assertThat(Response response) {
        return new ResponseAssert(response);
    }

    public ResponseAssert isBreakingContract(final int status, final ContractBrokenException cbe) {
        isNotNull();

        if (status != actual.getStatus()) {
            failWithMessage("Expected status to be <%s>, but was <%s>", status, actual.getStatus());
            return this;
        }

        String str = null;
        try {
            str = CharStreams.toString(new InputStreamReader((InputStream) actual.getEntity()));
        } catch (IOException e) {
            str = "";
        }

        if (!str.contains(cbe.getMessage())) {
            failWithMessage("Expected a message containing <%s> but got <%s>", cbe.getMessage(), str);
        }
        return this;
    }
}
