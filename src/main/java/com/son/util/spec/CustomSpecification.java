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
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case IN:
                if (criteria.getValue() instanceof List) {
                    return root.get(criteria.getKey()).in((List) criteria.getValue());
                }
                if (criteria.getValue() instanceof Integer[]) {
                    return root.get(criteria.getKey()).in((Integer[]) criteria.getValue());
                }
            case NOT_IN:
                if (criteria.getValue() instanceof List) {
                    return root.get(criteria.getKey()).in((List) criteria.getValue()).not();
                }
                if (criteria.getValue() instanceof Integer[]) {
                    return root.get(criteria.getKey()).in((Integer[]) criteria.getValue()).not();
                }
            default:
                return null;
        }
    }

}