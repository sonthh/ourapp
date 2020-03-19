package com.son.util.spec;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private boolean orPredicate;

    public SpecCriteria() {
    }

    public SpecCriteria(final String key, final SearchOperation operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecCriteria(final String orPredicate, final String key, final SearchOperation operation, final Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
