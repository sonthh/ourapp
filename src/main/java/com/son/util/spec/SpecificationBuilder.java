package com.son.util.spec;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {

    private final List<SpecCriteria> params;

    public SpecificationBuilder() {
        params = new ArrayList<>();
    }

    public final SpecificationBuilder query(
        final String key, final SearchOperation operation, final Object value
    ) {
        return query(null, key, operation, value, null);
    }

    public final SpecificationBuilder query(
            final String key, final SearchOperation operation, final Object value, final String joinField
    ) {
        return query(null, key, operation, value, joinField);
    }

    public final SpecificationBuilder query(
        final String orPredicate, final String key, SearchOperation op, final Object value, final String joinField
    ) {
        if (op == null || value == null) {
            return this;
        }

        params.add(new SpecCriteria(orPredicate, key, op, value, joinField));

        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        Specification<T> result = new CustomSpecification<T>(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                ? Specification.where(result).or(new CustomSpecification<>(params.get(i)))
                : Specification.where(result).and(new CustomSpecification<>(params.get(i)));
        }

        return result;
    }

    public final SpecificationBuilder query(CustomSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final SpecificationBuilder query(SpecCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
