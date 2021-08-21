/*
 * Copyright 2021 Apollo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package top.totalo.apollo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class ApolloThreadFactory implements ThreadFactory {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApolloThreadFactory.class);
    
    private final AtomicLong threadNumber = new AtomicLong(1);
    
    private final String namePrefix;
    
    private final boolean daemon;
    
    private static final ThreadGroup threadGroup = new ThreadGroup("Apollo");
    
    public static ThreadGroup getThreadGroup() {
        return threadGroup;
    }
    
    public static ThreadFactory create(String namePrefix, boolean daemon) {
        return new ApolloThreadFactory(namePrefix, daemon);
    }
    
    private interface ClassifyStandard<T> {
        boolean satisfy(T thread);
    }
    
    private static <T> void classify(Set<T> src, Set<T> des, ClassifyStandard<T> standard) {
        Set<T> set = new HashSet<>(); for (T t : src) {
            if (standard.satisfy(t)) {
                set.add(t);
            }
        } src.removeAll(set); des.addAll(set);
    }
    
    private ApolloThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix; this.daemon = daemon;
    }
    
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(threadGroup, runnable,//
                threadGroup.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement());
        thread.setDaemon(daemon); if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        } return thread;
    }
}
