package com.katalon.plugin.tags;

import org.osgi.service.event.Event;

import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.extension.EventListenerInitializer;

public class TagsEventListenerInitializer implements EventListenerInitializer {
	@Override
	public void registerListener(EventListener eventListener) {
		eventListener.on(Event.class, event -> {
			
		});
	}
}
