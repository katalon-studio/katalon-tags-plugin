package com.katalon.plugin.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.katalon.platform.api.console.PluginConsoleOption;
import com.katalon.platform.api.extension.LauncherOptionParserDescription;
import com.katalon.platform.api.model.TestCaseEntity;
import com.katalon.plugin.tags.constants.TagsConstants;
import com.katalon.plugin.tags.entity.StringConsoleOption;

public class TagsLauncherOptionParserDescription implements LauncherOptionParserDescription {

	private PluginConsoleOption<?> tagConsoleOption = new StringConsoleOption() {
		@Override
		public String getOption() {
			return TagsConstants.TAGS_CONSOLE_OPTION;
		}
		
		@Override
		public boolean isRequired(){
			return true;
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
		arg0.stream().forEach(a -> {
			if(Arrays.asList(a.getTags().split(",")).containsAll(Arrays.asList(tagConsoleOption.getValue().toString().split(",")))){
				System.out.println(a.getId() + " is a test case to be run");
				filteredTestCases.add(a);
			} else {
				System.out.println(a.getId() + " is filtered out ");
			}
		});
		return filteredTestCases;
	}

}
