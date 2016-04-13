configs-loader
=========

Configs loader is a maven plugin that loads configs from **( spring cloud config server )** and merge them with application configs during the maven build into one final config file **( application.yml )**.
The main purpose of this plugin is :
- Provide a full configuration file after the project is built .
- Any spring cloud config server changes won't affect the spring application unless the application is built again .

Use Case:
-------------------
This plugin is helpful in the following use case :

Lets assume we have config server **( CS )** and **( 20 )** microservices that uses it **( ms1 , .... , ms20 )**
if we change some configurations in **( CS )** then when any of the microservices is restarted it'll pick up the new configs ( which might be unwanted behaviour in case we want the CS changes to affect just specific microservices **( temporarily )** not all of them ).

With this plugin , the micro services will not pick thier configuration from config server at runtime , instead the plugin will pick up **CS** configurations , merge them with local configurations into one file **( all this at build time )** and the mircoservice will use this final merged file.

So we can rebuild just the microservices we want them to pick up the new configs .

configs-loader Features:
-------------------
Multiple options support: 
- Config server url and credintials.
- Local config file path.
- Final config file path.
- Ability to override config server configs from local ones.
- Specific profile retrieving form config server.

How to Use
==========

Just simply add the plugin to your **pom.xml** file , like the following :

```xml
  <plugin>
    <groupId>org.zlounge.phoenix.plugins</groupId>
    <artifactId>configs-loader-plugin</artifactId>
    <version>0.0.1</version>
    <configuration>
      <configServerUrl>${configServerUrl}</configServerUrl>
      <user>${user}</user>
      <password>${password}</profile>
      <profile>${profile}</profile>
      <outputFile>${outputFile}</outputFile>
      <localConfigFile>${localConfigFile}</localConfigFile>
      <overrideLocal>${overrideLocal}</overrideLocal>
    </configuration>

    <executions>
      <execution>
        <phase> clean </phase>
        <goals>
          <goal>load</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
```

Contributing to configs-loader
===============================

We welcome your ideas, issues, and pull requests. Just follow the
usual/standard GitHub practices.


License
===================

Copyright 2016 Zalando SE

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
