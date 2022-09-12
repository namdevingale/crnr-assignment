package com.crnr.hcms.assignment.query;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.crnr.hcms.assignment.dto.Filter;
import com.crnr.hcms.assignment.model.PatientModel;
import com.crnr.hcms.assignment.model.PatientModel_;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
public class ConditionalServicePredicate {

	public static List<Predicate> getFilterQueryForPatient(Filter filterContext, Root<PatientModel> patientModel,
			List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {

		if (!StringUtils.isEmpty(filterContext.getSearch()))
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(patientModel.get(PatientModel_.name)),
					"%" + filterContext.getSearch().toUpperCase() + "%"));

		if (!StringUtils.isEmpty(filterContext.getGender()))
			predicates.add(criteriaBuilder.equal(patientModel.get("gender"), filterContext.getGender().toUpperCase()));

		if (!StringUtils.isEmpty(filterContext.getStatus()))
			predicates.add(criteriaBuilder.equal(patientModel.get("status"), filterContext.getStatus().toUpperCase()));
		
		predicates.add(criteriaBuilder.equal(patientModel.get("isDeleted"), false));
		return predicates;
	}

}
