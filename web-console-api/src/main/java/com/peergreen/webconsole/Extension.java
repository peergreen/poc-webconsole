package com.peergreen.webconsole;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mohammed Boukada
 */
@Component
@HandlerDeclaration("<ns:extension xmlns:ns='com.peergreen.webconsole' />")
@Stereotype
@Target(ElementType.TYPE)
public @interface Extension {
}
