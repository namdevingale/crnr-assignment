package com.crnr.hcms.assignment.model.helper;

import java.io.Serializable;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
public interface Identifiable<T extends Serializable> {

	T getId();
}
