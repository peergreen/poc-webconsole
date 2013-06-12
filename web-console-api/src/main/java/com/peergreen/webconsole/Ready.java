package com.peergreen.webconsole;

import org.apache.felix.ipojo.annotations.Stereotype;
import org.apache.felix.ipojo.annotations.Validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author Mohammed Boukada
 */
@Validate
@Stereotype
@Target(ElementType.METHOD)
public @interface Ready {
}
