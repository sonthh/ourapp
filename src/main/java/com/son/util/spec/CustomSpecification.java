package com.son.util.spec;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Getter
public class CustomSpecification<T> implements Specification<T> {

    private SpecCriteria criteria;

    public CustomSpecification(final SpecCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Object value = criteria.getValue();
        String key = criteria.getKey();

        String[] tmp = key.split("\\.");
        String joinField = null;
        if (tmp.length == 2) {
            joinField = tmp[1];
            key = tmp[0];
        }

        switch (criteria.getOperation()) {
            case EQUALITY:
                if (joinField != null) {
                    return builder.equal(root.join(key).get(joinField), value);
                }
                return builder.equal(root.get(key), value);
            case NEGATION:
                if (joinField != null) {
                    return builder.notEqual(root.join(key).get(joinField), value);
                }
                return builder.notEqual(root.get(key), value);
            case GREATER_THAN:
                return builder.greaterThan(root.get(key), value.toString());
            case LESS_THAN:
                return builder.lessThan(root.get(key), value.toString());
            case LIKE:
                return builder.like(root.get(key), value.toString());
            case STARTS_WITH:
                if (joinField != null) {
                    return builder.like(root.join(key).get(joinField), "%" + value);
                }
                return builder.like(root.get(key), value + "%");
            case ENDS_WITH:
                if (joinField != null) {
                    return builder.like(root.join(key).get(joinField), value + "%");
                }
                return builder.like(root.get(key), "%" + value);
            case CONTAINS:
                if (joinField != null) {
                    return builder.like(root.join(key).get(joinField), "%" + value + "%");
                }
                return builder.like(root.get(key), "%" + value + "%");
            case IN:
                if (value instanceof List) {
                    return root.get(key).in((List<?>) value);
                }
            case NOT_IN:
                if (value instanceof List) {
                    return root.get(key).in((List<?>) value).not();
                }
            default:
                return null;
        }
    }
}
