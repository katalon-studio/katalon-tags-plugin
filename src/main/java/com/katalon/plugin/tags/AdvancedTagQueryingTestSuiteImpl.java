package com.katalon.plugin.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.katalon.platform.api.controller.FolderController;
import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.extension.DynamicQueryingTestSuiteDescription;
import com.katalon.platform.api.model.FolderEntity;
import com.katalon.platform.api.model.ProjectEntity;
import com.katalon.platform.api.model.TestCaseEntity;
import com.katalon.platform.api.model.TestSuiteEntity;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.plugin.tags.util.EntityTagUtil;

public class AdvancedTagQueryingTestSuiteImpl implements DynamicQueryingTestSuiteDescription {

    private static final List<String> DEFAULT_KEYWORDS = Arrays.asList("ids", "id", "name", "tag", "comment", "description", "tags");
    
    private static final String CONTENT_DELIMITER = ",";

    private FolderController folderController = ApplicationManager.getInstance().getControllerManager()
            .getController(FolderController.class);

    @Override
    public String getQueryingType() {
        return "Advanced Tag Search";
    }

    @Override
    public List<TestCaseEntity> query(ProjectEntity project, TestSuiteEntity testSuite, String fullSearchText)
            throws ResourceException {
        FolderEntity testCaseRoot = folderController.getFolder(project, "Test Cases");

        List<TestCaseEntity> allTestCases = getAllTestCases(project, testCaseRoot);
        return allTestCases.stream().filter(e -> isMatched(e, fullSearchText)).collect(Collectors.toList());
    }

    private List<TestCaseEntity> getAllTestCases(ProjectEntity project, FolderEntity parentFolder)
            throws ResourceException {
        List<TestCaseEntity> childTestCases = folderController.getChildTestCases(project, parentFolder);

        for (FolderEntity childFolder : folderController.getChildFolders(project, parentFolder)) {
            childTestCases.addAll(getAllTestCases(project, childFolder));
        }
        return childTestCases;
    }

    public List<String> getDefaultKeywords() {
        List<String> keywords = new ArrayList<>();
        keywords.addAll(DEFAULT_KEYWORDS);
        return keywords;
    }

    public boolean isMatched(TestCaseEntity testCase, String filteringText) {
        String trimmedText = filteringText.trim();
        if (trimmedText.equals(StringUtils.EMPTY)) {
            return false;
        }
        List<String> keywordList = getDefaultKeywords();
        Map<String, String> tagMap = parseSearchedString(keywordList.toArray(new String[0]), trimmedText);

        if (!tagMap.isEmpty()) {
            for (Entry<String, String> entry : tagMap.entrySet()) {
                String keyword = entry.getKey();
                if (keywordList.contains(keyword) && !compare(testCase, keyword, entry.getValue())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * parse searched string into a map of search tags of an entity element
     * 
     * @param element
     *            is ITreeEntity
     * @return
     */
    public Map<String, String> parseSearchedString(String[] searchTags, String contentString) {
        if (searchTags != null) {
            Map<String, String> tagMap = new HashMap<String, String>();
            for (int i = 0; i < searchTags.length; i++) {
                String tagRegex = searchTags[i] + "=\\([^\\)]+\\)";
                Matcher m = Pattern.compile(tagRegex).matcher(contentString);
                while (m.find()) {
                    String tagContent = contentString.substring(m.start() + searchTags[i].length() + 2, m.end() - 1);
                    tagMap.put(searchTags[i], tagContent);
                }
            }
            return tagMap;
        } else {
            return Collections.emptyMap();
        }

    }

    public boolean compare(TestCaseEntity fileEntity, String keyword, String text) {
        if (fileEntity == null || keyword == null || text == null) {
            return false;
        }
        switch (keyword) {
            case "ids":
                return textContainsEntityId(text.toLowerCase(), fileEntity);
            case "id":
                return StringUtils.equalsIgnoreCase(fileEntity.getId(), text)
                        || StringUtils.startsWithIgnoreCase(fileEntity.getId(), text + "/");
            case "name":
                return StringUtils.containsIgnoreCase(fileEntity.getName(), text);
            case "tag":
                return StringUtils.containsIgnoreCase(fileEntity.getTags(), text);
            case "description":
                return StringUtils.containsIgnoreCase(fileEntity.getDescription(), text);
            case "tags":
                return hasTags(fileEntity, text);
            default:
                return false;
        }
    }
    
    private boolean textContainsEntityId(String text, TestCaseEntity testCase) {
        return Arrays.asList(text.split(CONTENT_DELIMITER))
                .stream()
                .map(a -> a.trim())
                .filter(a -> StringUtils.equalsIgnoreCase(testCase.getId(), a)
                        || StringUtils.startsWithIgnoreCase(testCase.getId(), a + "/"))
                .findAny()
                .isPresent();
    }

    private boolean hasTags(TestCaseEntity testCase, String searchTagValues) {
        if (StringUtils.isBlank(searchTagValues)) {
            return true;
        }

        String entityTagValues = testCase.getTags();
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
