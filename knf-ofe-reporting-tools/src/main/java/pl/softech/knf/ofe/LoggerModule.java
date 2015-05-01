/*
 * Copyright 2015 Sławomir Śledź <slawomir.sledz@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.knf.ofe;

import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class LoggerModule extends AbstractModule {

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new Slf4JTypeListener());
    }

    private static class Slf4JTypeListener implements TypeListener {

        @Override
        public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {

            Class<?> clazz = typeLiteral.getRawType();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getType() == Logger.class &&
                            field.isAnnotationPresent(InjectLogger.class)) {
                        typeEncounter.register(new Slf4JMembersInjector<I>(field));
                    }
                }
                clazz = clazz.getSuperclass();
            }

        }
    }

    private static class Slf4JMembersInjector<T> implements MembersInjector<T> {

        private final Field field;
        private final Logger logger;

        Slf4JMembersInjector(Field field) {
            this.field = field;
            this.logger = LoggerFactory.getLogger(field.getDeclaringClass());
            field.setAccessible(true);
        }

        @Override
        public void injectMembers(T instance) {
            try {
                field.set(instance, logger);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
