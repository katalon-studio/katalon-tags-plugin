package com.katalon.plugin.virtual_test_suite;

import java.util.Arrays;

import org.osgi.service.event.Event;

import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.extension.EventListenerInitializer;
import com.katalon.platform.api.model.Entity;
import com.katalon.platform.api.model.PluginConsoleOption;
import com.katalon.platform.api.model.PluginConsoleOptionContributor;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.plugin.virtual_test_suite.constant.VirtualTestSuiteConstants;
import com.katalon.plugin.virtual_test_suite.entity.StringConsoleOption;

public class VirtualTestSuiteEventListenerInitializer implements EventListenerInitializer {
	@Override
	public void registerListener(EventListener eventListener) {
		eventListener.on(Event.class, event -> {
			if(event.getTopic().equals("KATALON_PLATFORM/PROJECT/CURRENT_PROJECT_CHANGED")){
				Entity projectEntity = ApplicationManager.getInstance().getProjectManager().getCurrentProject();
				if (projectEntity != null) {
					try {
						PluginConsoleOptionContributor consoleOptionContributor = ApplicationManager.
								getInstance().
								getConsoleOptionManager().
								getConsoleOption(projectEntity.getId(),
								"com.katalon.katalon-studio-virtual-test-suite");
						
						PluginConsoleOption<String> virtualTestCases = new StringConsoleOption() {
							@Override
							public String getOption() {
								return VirtualTestSuiteConstants.TEST_CASES_INCLUDED;
							}
							
							public boolean isRequired(){
								return false;
							}
						};
						consoleOptionContributor.setConsoleOptionList(Arrays.asList(virtualTestCases));
					} catch (ResourceException e) {
						e.printStackTrace(System.out);
					}
				}
			}
		});
	}
}
