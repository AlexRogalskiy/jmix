/*
 * Copyright 2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.ui.navigation;

/**
 * Marker interface for beans that manage whether some route is allowed navigate to or not.
 */
public interface NavigationFilter {

    /**
     * Checks whether {@code fromState} to {@code toState} navigation is allowed or not.
     *
     * @param fromState from state
     * @param toState   to state
     * @return true if requested state can be navigated, false otherwise
     */
    AccessCheckResult allowed(NavigationState fromState, NavigationState toState);

    class AccessCheckResult {

        protected final boolean allowed;
        protected final String message;

        public AccessCheckResult(boolean allowed) {
            this(allowed, "");
        }

        public AccessCheckResult(boolean allowed, String message) {
            this.allowed = allowed;
            this.message = message;
        }

        public static AccessCheckResult allowed() {
            return new AccessCheckResult(true, "");
        }

        public static AccessCheckResult rejected() {
            return rejected("");
        }

        public static AccessCheckResult rejected(String msg) {
            return new AccessCheckResult(false, msg);
        }

        public boolean isAllowed() {
            return allowed;
        }

        public boolean isRejected() {
            return !isAllowed();
        }

        public String getMessage() {
            return message;
        }
    }
}
