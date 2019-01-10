package com.katalon.plugin.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.katalon.platform.api.extension.PluginConsoleOptionRegister;
import com.katalon.platform.api.model.PluginConsoleOption;
import com.katalon.platform.api.model.TestCaseEntity;
import com.katalon.plugin.tags.constants.TagsConstants;
import com.katalon.plugin.tags.entity.StringConsoleOption;

public class TagsConsoleOptionRegister implements PluginConsoleOptionRegister {

	@Override
	public List<TestCaseEntity>  filterTestCasesOnPluginConsoleOptionRecognized(PluginConsoleOption<?> arg0, List<TestCaseEntity> arg1) {
		List<TestCaseEntity> testCasesToBeExecuted = new ArrayList<>();
		if(arg0.getOption().equals(TagsConstants.TAGS_CONSOLE_OPTION)){
			String value = (String) arg0.getValue();
			for(TestCaseEntity testCaseEntity : arg1){
				String strTags = testCaseEntity.getTags();
				List<String> tags =  Arrays.asList(strTags.split(","));
				if(tags.contains(value)){
					testCasesToBeExecuted.add(testCaseEntity);
				}
			}
		}
		return testCasesToBeExecuted;
	}

	@Override
	public List<PluginConsoleOption<?>> getPluginConsoleOptionList() {
		return Arrays.asList(new StringConsoleOption(){
			@Override
			public String getOption() {
				return TagsConstants.TAGS_CONSOLE_OPTION;
			}
			
		});
	}
	

}
