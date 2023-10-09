package com.github.ldeitos.restutil;

import java.beans.ParameterDescriptor;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

/**
 * Descriptor to parsed parameters obtained from {@link ParametersParser}.
 *
 * @author <a href="mailto:leandro.deitos@gmail.com">Leandro Deitos</a>
 *
 */
public class ParamDescriptor implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Transformer<ParamDescriptor, String> TO_PARAM_NAME = ParamDescriptor::getParamName;

	private String paramName;

	private List<ParamDescriptor> innerParams = new ArrayList<>();

	ParamDescriptor() {
		super();
		this.paramName = StringUtils.EMPTY;
	}

	public ParamDescriptor(String paramName) {
		super();

		if (StringUtils.isBlank(paramName) || Pattern.matches("\\W", paramName)) {
			throw new InvalidParameterException(String.format("paramName [%s] must be a valid alphanumeric word.",
					paramName));
		}

		this.paramName = paramName;
	}

	/**
	 * If current parameter is complex, return correspondent inner
	 * {@link ParameterDescriptor}, or null if {@link #isSimple()}.
	 *
	 * @param paramName
	 *                  Name from parameter to obtain {@link ParamDescriptor}.
	 *
	 * @return Correspondent paramName {@link ParameterDescriptor}.
	 *
	 * @see #isComplex()
	 * @see #isSimple()
	 */
	public ParamDescriptor getInnerParam(final String paramName) {
		return CollectionUtils.find(this.innerParams, object -> object.getParamName().equals(paramName));
	}

	/**
	 * @return If current parameter is complex, return list of
	 *         {@link ParamDescriptor} from inner parameters.
	 *
	 * @see #isComplex()
	 * @see #isSimple()
	 */
	public List<ParamDescriptor> getInnerParams() {
		return this.innerParams;
	}

	/**
	 * @return Parameter name.
	 */
	public String getParamName() {
		return this.paramName;
	}

	/**
	 * @return True if parameter contain other parameters.
	 */
	public boolean isComplex() {
		return !this.isSimple();
	}

	/**
	 * @return True if parameter does not contain other parameters.
	 */
	public boolean isSimple() {
		return this.innerParams.isEmpty();
	}

	/**
	 * @return True if parameter does not contain name.
	 */
	public boolean isRoot() {
		return StringUtils.isBlank(this.paramName);
	}

	@Override
	public String toString() {
		return this.isRoot() ? "root" : this.paramName;
	}

	public String[] getInnerParamsNames() {
		List<String> innerParamsNames = new ArrayList<>(
				CollectionUtils.collect(this.innerParams, ParamDescriptor.TO_PARAM_NAME));
		return innerParamsNames.toArray(new String[innerParamsNames.size()]);
	}

	/**
	 * @return String representation from current instance.
	 *
	 * @since 1.0.CR2
	 */
	public String asString() {
		StringBuilder sb = new StringBuilder(this.getParamName());

		if (this.isComplex()) {
			if (!this.isRoot()) {
				sb.append("(");
			}

			Iterator<ParamDescriptor> it = this.innerParams.iterator();
			while (it.hasNext()) {
				ParamDescriptor param = it.next();
				sb.append(param.asString());
				if (it.hasNext()) {
					sb.append(",");
				}
			}

			if (!this.isRoot()) {
				sb.append(")");
			}
		}

		return sb.toString();
	}
}
