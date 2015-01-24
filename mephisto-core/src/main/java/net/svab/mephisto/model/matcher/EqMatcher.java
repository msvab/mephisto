package net.svab.mephisto.model.matcher;

import com.google.common.base.Objects;

public class EqMatcher implements Matcher {

    private final Object value;

    public EqMatcher(Object value) {
        this.value = value;
    }

    @Override public boolean matches(Object value) {
        return Objects.equal(this.value, value);
    }

    public static Matcher eq(Object value) {
        return new EqMatcher(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EqMatcher eqMatcher = (EqMatcher) o;
        return !(value != null ? !value.equals(eqMatcher.value) : eqMatcher.value != null);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override public String toString() {
        return String.valueOf(value);
    }
}
