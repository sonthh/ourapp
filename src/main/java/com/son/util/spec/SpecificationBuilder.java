package com.son.util.spec;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {

    private final List<SpecCriteria> params;

    public SpecificationBuilder() {
        params = new ArrayList<>();
    }

    public final SpecificationBuilder with(final String key, final SearchOperation operation, final Object value,
                                           final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final SpecificationBuilder with(final String key, final SearchOperation operation, final Object value) {
        return with(null, key, operation, value, null, null);
    }

    public final SpecificationBuilder with(final String orPredicate, final String key, SearchOperation op,
                                           final Object value, final String prefix, final String suffix) {
        if (op == null) {
            return this;
        }

        if (op == SearchOperation.EQUALITY) {
            final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
            final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

            if (startWithAsterisk && endWithAsterisk) {
                op = SearchOperation.CONTAINS;
            } else if (startWithAsterisk) {
                op = SearchOperation.ENDS_WITH;
            } else if (endWithAsterisk) {
                op = SearchOperation.STARTS_WITH;
            }
        }

        params.add(new SpecCriteria(orPredicate, key, op, value));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0)
            return null;

        Specification<T> result = new CustomSpecification<T>(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new CustomSpecification<>(params.get(i)))
                    : Specification.where(result).and(new CustomSpecification<>(params.get(i)));
        }

        return result;
    }

    public final SpecificationBuilder with(CustomSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final SpecificationBuilder with(SpecCriteria criteria) {
        params.add(criteria);
        return this;
    }
}