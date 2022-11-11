/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.boot.properties.api;


import org.junit.jupiter.api.Test;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.properties.tree.Properties;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBootApplicationPropertiesTest {

    @Test
    void createNewProperties_Add_Print() {
        SpringBootApplicationProperties sut = SpringBootApplicationProperties.newApplicationProperties(Path.of("./projectDir").toAbsolutePath(), Path.of("./fake2.properties"));
        sut.setProperty("some", "property");
        sut.setProperty("another", "foo");
        assertThat(sut.print()).isEqualTo("some=property\n" +
                "another=foo");
    }

    @Test
    void parseExistingPropertiesTest() {
        List<Properties.File> parse = new PropertiesParser().parse(
                "foo=bar\n" +
                        "bob=bill");
        SpringBootApplicationProperties sut = new SpringBootApplicationProperties(Path.of("./projectDir").toAbsolutePath(), parse.get(0));
        assertThat(sut.getProperty("foo").get()).isEqualTo("bar");
        assertThat(sut.getProperty("bob").get()).isEqualTo("bill");
        assertThat(sut.getProperty("jane")).isEmpty();
    }

    @Test
    void shouldSupportYaml() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource("src/main/resources/application.yaml", "a.b: c")
                .build();
        RewriteSourceFileHolder r = (RewriteSourceFileHolder) context.getProjectResources().get(1);

//        SpringBootApplicationProperties sut = new SpringBootApplicationProperties(TestProjectContext.getDefaultProjectRoot(), r);
    }

}