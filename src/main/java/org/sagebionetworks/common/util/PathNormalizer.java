package org.sagebionetworks.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathNormalizer {
	private static final String AUTH_V1 = "/auth/v1";
	private static final String FILE_V1 = "/file/v1";
	private static final String REPO_V1 = "/repo/v1";
	
	private static final Pattern NUMERIC_PARAM_PATTERN = Pattern.compile("/(syn\\d+|\\d+)");
	private static final String GET_MD5_URL_PART = "/entity/md5";
	private static final String GET_EVALUATION_NAME_URL_PART = "/evaluation/name";
	private static final String GET_ENTITY_ALIAS_URL_PART = "/entity/alias";
	private static final String NUMBER_REPLACEMENT = "/#";
	
	/**
	 * Normalize the access record's request path
	 * 
	 * @param requestPath
	 * @param method
	 * @return
	 */
	public static String normalizeMethodSignature(String requestPath, String method) {
		requestPath = requestPath.toLowerCase();
		if (requestPath.startsWith(REPO_V1) || requestPath.startsWith(FILE_V1) || requestPath.startsWith(AUTH_V1)) {
			requestPath = requestPath.substring(8);
		}
		if (method.equals("GET") && requestPath.startsWith(GET_MD5_URL_PART)) {
			return GET_MD5_URL_PART + NUMBER_REPLACEMENT;
		}
		if (method.equals("GET") && requestPath.startsWith(GET_EVALUATION_NAME_URL_PART)) {
			return GET_EVALUATION_NAME_URL_PART + NUMBER_REPLACEMENT;
		}
		if (method.equals("GET") && requestPath.startsWith(GET_ENTITY_ALIAS_URL_PART)) {
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
