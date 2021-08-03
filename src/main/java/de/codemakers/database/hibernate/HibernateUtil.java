/*
 *    Copyright 2021 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.database.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Objects;
import java.util.Properties;

public class HibernateUtil {
    
    public static StandardServiceRegistryBuilder createStandardServiceRegistryBuilder(Properties properties) {
        Objects.requireNonNull(properties);
        final StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
        standardServiceRegistryBuilder.applySettings(properties);
        return standardServiceRegistryBuilder;
    }
    
    public static StandardServiceRegistry createStandardServiceRegistry(StandardServiceRegistryBuilder builder) {
        Objects.requireNonNull(builder);
        return builder.build();
    }
    
    public static MetadataSources createMetadataSources(StandardServiceRegistry registry, Class<?>[] annotatedClasses) {
        Objects.requireNonNull(registry);
        Objects.requireNonNull(annotatedClasses);
        final MetadataSources metadataSources = new MetadataSources(registry);
        for (Class<?> annotatedClass : annotatedClasses) {
            metadataSources.addAnnotatedClass(annotatedClass);
        }
        return metadataSources;
    }
    
    public static Metadata createMetadata(MetadataSources metadataSources) {
        Objects.requireNonNull(metadataSources);
        return metadataSources.buildMetadata();
    }
    
    public static SessionFactory createSessionFactory(Metadata metadata) {
        Objects.requireNonNull(metadata);
        return metadata.buildSessionFactory();
    }
    
}
