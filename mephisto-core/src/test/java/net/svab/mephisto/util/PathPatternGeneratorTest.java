package net.svab.mephisto.util;

import org.junit.Test;
import net.svab.mephisto.model.ParameterType;
import net.svab.mephisto.model.UriParameter;

import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static net.svab.mephisto.model.ResourceBuilder.resourceBuilder;

public class PathPatternGeneratorTest {

    @Test
    public void shouldCreatePatternForStringParamAtTheEnd() {
        Pattern pattern = PathPatternGenerator.pathPattern(resourceBuilder().path("/some/{string}").uriParameter(new UriParameter("string", ParameterType.STRING)).build());

        assertThat(pattern.pattern()).isEqualTo("^/some/[^/]+$");
    }

    @Test
    public void shouldCreatePatternForStringParamInTheMiddle() {
        Pattern pattern = PathPatternGenerator.pathPattern(resourceBuilder().path("/some/{string}/end").uriParameter((new UriParameter("string", ParameterType.STRING))).build());

        assertThat(pattern.pattern()).isEqualTo("^/some/[^/]+/end$");
    }

    @Test
    public void shouldCreatePatternForInteger() {
        Pattern pattern = PathPatternGenerator.pathPattern(resourceBuilder().path("/some/{int}").uriParameter((new UriParameter("int", ParameterType.INTEGER))).build());

        assertThat(pattern.pattern()).isEqualTo("^/some/\\d+(?![^/])$");
    }

    @Test
    public void shouldCreatePatternForNumber() {
        Pattern pattern = PathPatternGenerator.pathPattern(resourceBuilder().path("/some/{num}").uriParameter((new UriParameter("num", ParameterType.NUMBER))).build());

        assertThat(pattern.pattern()).isEqualTo("^/some/\\d+(\\.\\d+)?(?![^/])$");
    }

    @Test
    public void shouldCreatePatternForBoolean() {
        Pattern pattern = PathPatternGenerator.pathPattern(resourceBuilder().path("/some/{bool}").uriParameter(new UriParameter("bool", ParameterType.BOOLEAN)).build());

        assertThat(pattern.pattern()).isEqualTo("^/some/(true|false)$");
    }

    @Test
    public void shouldCreatePatternForStringEnum() {
        Pattern pattern = PathPatternGenerator.pathPattern(resourceBuilder().path("/some/{str}").uriParameter(new UriParameter("str", ParameterType.STRING, asList("foo", "bar"))).build());

        assertThat(pattern.pattern()).isEqualTo("^/some/(foo|bar)$");
    }

    @Test
    public void shouldCreatePatternForIntegerEnum() {
        Pattern pattern = PathPatternGenerator.pathPattern(resourceBuilder().path("/some/{int}").uriParameter(new UriParameter("int", ParameterType.INTEGER, asList("1", "2"))).build());

        assertThat(pattern.pattern()).isEqualTo("^/some/(1|2)$");
    }
}