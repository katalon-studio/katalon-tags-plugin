package com.katalon.plugin.tags;

import java.util.Arrays;
import java.util.List;

import com.katalon.platform.api.console.PluginConsoleOption;
import com.katalon.platform.api.extension.LauncherOptionParserDescription;
import com.katalon.platform.api.model.TestSuiteEntity;
import com.katalon.plugin.tags.constants.TagsConstants;
import com.katalon.plugin.tags.entity.StringConsoleOption;

public class TagsConsoleOptionRegister implements LauncherOptionParserDescription {

	@Override
	public List<PluginConsoleOption<?>> getConsoleOptionList() {
		return Arrays.asList(new StringConsoleOption(){
			@Override
			public String getOption() {
				return TagsConstants.TAGS_CONSOLE_OPTION;
			}
		});
	}

	@Override
	public void onConsoleOptionDetected(com.katalon.platform.api.console.PluginConsoleOption<?> arg0) {
		if(arg0.getOption().equals(TagsConstants.TAGS_CONSOLE_OPTION)){
			System.out.println(TagsConstants.TAGS_CONSOLE_OPTION 
					+ " is recognized with value " + arg0.getValue());
		}
	}

	@Override
	public void preExecution(TestSuiteEntity arg0) {
		System.out.println(arg0.getParentFolderId());
	}

}
