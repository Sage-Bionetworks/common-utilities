package org.sagebionetworks.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for normalizing request paths
 */
public class PathNormalizer {
	private static final String V1_PREFIX = "/v1/";
	
	private static final Pattern NUMERIC_PARAM_PATTERN = Pattern.compile("/(syn\\d+|\\d+)");
	private static final String GET_MD5_URL_PART = "/entity/md5";
	private static final String GET_EVALUATION_NAME_URL_PART = "/evaluation/name";
	private static final String GET_ENTITY_ALIAS_URL_PART = "/entity/alias";
	private static final String NUMBER_REPLACEMENT = "/#";
	
	/**
	 * Normalize the access record's request path
	 * 
	 * @param requestPath
	 * @return
	 */
	public static String normalizeMethodSignature(String requestPath) {
		requestPath = requestPath.toLowerCase();
		int indexOfValidPathPrefix = requestPath.indexOf(V1_PREFIX);
		if (indexOfValidPathPrefix == -1) {
			throw new IllegalArgumentException("requestPath must contain " + V1_PREFIX);
		}
		
		requestPath = requestPath.substring(indexOfValidPathPrefix + V1_PREFIX.length() - 1);

		if (requestPath.startsWith(GET_MD5_URL_PART)) {
			return GET_MD5_URL_PART + NUMBER_REPLACEMENT;
		}
		if (requestPath.startsWith(GET_EVALUATION_NAME_URL_PART)) {
			return GET_EVALUATION_NAME_URL_PART + NUMBER_REPLACEMENT;
		}
		if (requestPath.startsWith(GET_ENTITY_ALIAS_URL_PART)) {
			return GET_ENTITY_ALIAS_URL_PART + NUMBER_REPLACEMENT;
		}
		Matcher matcher = NUMERIC_PARAM_PATTERN.matcher(requestPath);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(buffer, NUMBER_REPLACEMENT);
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}
}
