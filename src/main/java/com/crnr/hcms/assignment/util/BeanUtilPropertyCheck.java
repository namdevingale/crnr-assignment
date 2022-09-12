package com.crnr.hcms.assignment.util;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Program Name: BeanUtilPropertyCheck
 *
 * Program Description/functionality:This class is to identify the null property 
 * for ignoring the given "ignoreProperties" in BeanUtils.copyProperties
 * into application.
 *
 * Developer Created/Modified Date Purpose
 *******************************************************************************
 * @author Namadev 07/09/2022
 * 
 * @version 1.0
 *
 */
public class BeanUtilPropertyCheck {

	public static String[] getNullPropertyNames (Object source) {
	    final BeanWrapper beanWrapperSrc = new BeanWrapperImpl(source);
	    PropertyDescriptor[] propertyDesc = beanWrapperSrc.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for(PropertyDescriptor pd : propertyDesc) {
	        Object srcValue = beanWrapperSrc.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }

	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
}
