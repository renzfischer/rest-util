package com.github.ldeitos.tarcius.config;

import static com.github.ldeitos.tarcius.config.ConfigConstants.CUSTOM_RESOLVER;
import static com.github.ldeitos.tarcius.config.ConfigConstants.JSON_RESOLVER;
import static com.github.ldeitos.tarcius.config.ConfigConstants.STRING_RESOLVER;
import static com.github.ldeitos.tarcius.config.ConfigConstants.XML_RESOLVER;

import com.github.ldeitos.tarcius.api.ParameterResolver;
import com.github.ldeitos.tarcius.qualifier.CustomResolver;

/**
 * Translation types for the parameters to be audited.
 *
 * @author <a href=mailto:leandro.deitos@gmail.com>Leandro Deitos</a>
 *
 */
public enum TranslateType {
	/**
	 * Default type, audited parameter value is resolved by Sting.valueOf();
	 */
	STRING_VALUE(STRING_RESOLVER),
	/**
	 * Audited parameter value is resolved to XML format by JAXB API
	 */
	JAXB_XML(XML_RESOLVER),
	/**
	 * Audited parameter value is resolved to JSON format by JAXB API
	 */
	JAXB_JSON(JSON_RESOLVER),
	/**
	 * Audited parameter values is resolved by custom {@link ParameterResolver}
	 */
	CUSTOM(CUSTOM_RESOLVER);

	private CustomResolver resolverQualifier;

	private TranslateType(CustomResolver resolver) {
		resolverQualifier = resolver;
	}

	public CustomResolver getResolverQualifier() {
		return resolverQualifier;
	}

}
