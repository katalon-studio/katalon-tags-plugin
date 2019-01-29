package com.katalon.plugin.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.katalon.platform.api.console.PluginConsoleOption;
import com.katalon.platform.api.extension.LauncherOptionParserDescription;
import com.katalon.platform.api.model.TestCaseEntity;
import com.katalon.plugin.tags.constants.TagsConstants;
import com.katalon.plugin.tags.entity.StringConsoleOption;
import com.katalon.plugin.tags.util.EntityTagUtil;

public class TagsLauncherOptionParserDescription implements LauncherOptionParserDescription {

	private PluginConsoleOption<?> tagConsoleOption = new StringConsoleOption() {
		@Override
		public String getOption() {
			return TagsConstants.TAGS_CONSOLE_OPTION;
		}

		@Override
		public boolean isRequired() {
			return false;
		}
	};

	@Override
	public List<PluginConsoleOption<?>> getConsoleOptionList() {
		return Arrays.asList(tagConsoleOption);
	}

	@Override
	public void onConsoleOptionDetected(com.katalon.platform.api.console.PluginConsoleOption<?> arg0) {
		if (arg0.getOption().equals(tagConsoleOption.getOption())) {
			tagConsoleOption.setValue((String) arg0.getValue());
		}
	}

	@Override
	public List<TestCaseEntity> onPreExecution(List<TestCaseEntity> arg0) {
		List<TestCaseEntity> filteredTestCases = new ArrayList<>();
		if (tagConsoleOption.getValue() == null) {
			return arg0;
		}
		System.out.println("----------------- TEST CASE TAGS PLUGIN START FILTERING -----------------");
		arg0.stream().forEach(a -> {
			if (hasTags(a.getTags(), tagConsoleOption.getValue().toString())) {
				System.out.println(a.getId() + " is a test case to be run");
				filteredTestCases.add(a);
			} else {
				System.out.println(a.getId() + " is filtered out ");
			}
		});
		System.out.println("----------------- TEST CASE TAGS PLUGIN FINISH FILTERING -----------------");
		return filteredTestCases;
	}

	private boolean hasTags(String entityTagValues, String searchTagValues) {

		if (StringUtils.isBlank(searchTagValues)) {
			return true;
		}

		if (StringUtils.isBlank(entityTagValues)) {
			return false;
		}

		Set<String> searchTags = EntityTagUtil.parse(searchTagValues).stream().map(tag -> tag.toLowerCase())
				.collect(Collectors.toSet());

		Set<String> entityTags = EntityTagUtil.parse(entityTagValues).stream().map(tag -> tag.toLowerCase())
				.collect(Collectors.toSet());

		return entityTags.containsAll(searchTags);
	}

}
