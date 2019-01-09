## katalon-tags-plugin

The main purpose is supporting Katalon - JIRA integration:

- Manage and search test cases by tags.
- Automatically added jira-integration tag when creating jira-integrated test cases.
- Execute test cases with specified tags.

#### Build
Requirements:

- JDK 1.8
- Maven 3.3+

Build

`mvn clean package`

#### How to test in Katalon Studio

- Checkout or get a build of branch `staging-plugin` of KS
- After KS opens, please click on `Plugin` menu, select `Install Plugin` and choose the generated jar file.
- If you want to reload this plugin, please click on `Plugin` menu, select `Uninstall Plugin` then select `Install Plugin` again. 
