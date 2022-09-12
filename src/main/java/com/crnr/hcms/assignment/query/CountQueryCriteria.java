package com.crnr.hcms.assignment.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
public class CountQueryCriteria {

	public static <T> Long createCountQuery(final CriteriaBuilder criteriaBuilder, final CriteriaQuery<T> criteria,
			final Root<T> root, final EntityManager entityManager) {
		final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		final Root<T> countRoot = countQuery.from(criteria.getResultType());
		
		doJoins(root.getJoins(), countRoot);
		doJoinsOnFetches(root.getFetches(), countRoot);
		
		countQuery.select(criteriaBuilder.count(countRoot));
		countQuery.where(criteria.getRestriction());
		
		countRoot.alias(root.getAlias());
		
		countQuery.distinct(criteria.isDistinct());
		return entityManager.createQuery(countQuery.distinct(criteria.isDistinct())).getSingleResult();
	}

	private static void doJoins(Set<? extends Join<?, ?>> joins, Root<?> root) {
		for (Join<?, ?> join : joins) {
			Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
			joined.alias(join.getAlias());
			doJoins(join.getJoins(), joined);
		}
	}

	@SuppressWarnings("unchecked")
	private static void doJoinsOnFetches(Set<? extends Fetch<?, ?>> joins, Root<?> root) {
		doJoins((Set<? extends Join<?, ?>>) joins, root);
	}

	private static void doJoins(Set<? extends Join<?, ?>> joins, Join<?, ?> root) {
		for (Join<?, ?> join : joins) {
			Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
			joined.alias(join.getAlias());
			doJoins(join.getJoins(), joined);
		}
	}

	public static List<Order> getOrderList(String order, String value, CriteriaBuilder criteriaBuilder,
			Root<?> gameInfo) {
		List<Order> orderList = new ArrayList<Order>();
		if (order.equalsIgnoreCase("ASC"))
			orderList.add(criteriaBuilder.asc(gameInfo.get(value)));
		else if (order.equalsIgnoreCase("DESC"))
			orderList.add(criteriaBuilder.desc(gameInfo.get(value)));
		return orderList;
	}
	
}
