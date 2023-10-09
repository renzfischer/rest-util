package com.github.ldeitos.restutil;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Parser to convert a String in {@link ParamDescriptor}.
 *
 * @author <a href="mailto:leandro.deitos@gmail.com">Leandro Deitos</a>
 *
 */
public class ParametersParser {

	private static final String COMPLEX_PARAM_BEGGIN = "(";

	private static final String COMPLEX_PARAM_END = ")";

	private static final Pattern SIMPLE_PARAM = Pattern.compile("(\\w+)");

	private static final int GROUP_PARAM = 1;

	/**
	 * @param params
	 *               String representing parameter list. Must follow the
	 *               pattern:<br>
	 *               <br>
	 *               <ul>
	 *               <li>Each parameter must be a valid alphanumeric word.</li>
	 *               <li>Each parameter must be separated by comma.</li>
	 *               <li>Complex parameters must describe yours parameters under
	 *               parentheses.</li>
	 *               </ul>
	 *               Representation example:
	 *               <i>"kind,items(title,characteristics(length))"</i>
	 *
	 * @return Root {@link ParamDescriptor} with parameters list in your
	 *         {@link ParamDescriptor#getInnerParams()}
	 */
	public static ParamDescriptor parse(String params) {
		ParamDescriptor toReturn = new ParamDescriptor();
		Deque<List<ParamDescriptor>> paramsStack = new LinkedList<>();
		paramsStack.add(toReturn.getInnerParams());
		String toParse = params == null ? null : params;

		while (StringUtils.isNotBlank(toParse)) {
			Matcher matcher = ParametersParser.SIMPLE_PARAM.matcher(toParse);

			if (!matcher.find()) {
				break;
			}

			String paramName = matcher.group(ParametersParser.GROUP_PARAM);
			ParamDescriptor param = new ParamDescriptor(paramName);

			paramsStack.peekLast().add(param);
			toParse = matcher.replaceFirst(StringUtils.EMPTY).trim();

			if (StringUtils.startsWith(toParse, ParametersParser.COMPLEX_PARAM_BEGGIN)) {
				paramsStack.add(param.getInnerParams());
			}

			while (StringUtils.startsWith(toParse, ParametersParser.COMPLEX_PARAM_END)) {
				paramsStack.pollLast();
				toParse = StringUtils.substring(toParse, 1);
			}

			toParse = StringUtils.substring(toParse, 1);
		}

		return toReturn;
	}

}
