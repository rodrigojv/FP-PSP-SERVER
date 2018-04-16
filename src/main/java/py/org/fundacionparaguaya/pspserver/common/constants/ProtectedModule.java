package py.org.fundacionparaguaya.pspserver.common.constants;

import py.org.fundacionparaguaya.pspserver.security.constants.Role;

import static py.org.fundacionparaguaya.pspserver.common.constants.ProtectedResource.API_ROOT;

/**
 * Created by rodrigovillalba on 11/23/17.
 */
public enum ProtectedModule implements ProtectableModule {

    ORGANIZATIONS {
        private String[] urls = { API_ROOT + "/activities", API_ROOT + "/activities/**", API_ROOT + "/applications",
                API_ROOT + "/applications/**", API_ROOT + "/organizations", API_ROOT + "/organizations/**",
                API_ROOT + "/user-applications", API_ROOT + "/users-applications/**", API_ROOT + "/users",
                API_ROOT + "/users/**", API_ROOT + "/user-roles", API_ROOT + "/user-roles/**", API_ROOT + "/families",
                API_ROOT + "/families/**", API_ROOT + "/cities", API_ROOT + "/cities/**", API_ROOT + "/countries",
                API_ROOT + "/countries/**", API_ROOT + "/parameters", API_ROOT + "/parameters/**", API_ROOT + "/people",
                API_ROOT + "/people/**", };

        private String[] readRoles = { Role.ROLE_ROOT.getSecurityName(), Role.ROLE_USER.getSecurityName() };

        private String[] writeRoles = { Role.ROLE_ROOT.getSecurityName(), Role.ROLE_APP_ADMIN.getSecurityName(),
                Role.ROLE_HUB_ADMIN.getSecurityName() };

        @Override
        public String[] getUrls() {
            return urls;
        }

        @Override
        public String[] getReadRoles() {
            return readRoles;
        }

        @Override
        public String[] getWriteRoles() {
            return writeRoles;
        }

    },
    SURVEYS {
        private String[] urls = { API_ROOT + "/surveys", API_ROOT + "/surveys/**", API_ROOT + "/snapshots",
                API_ROOT + "/snapshots/**" };

        private String[] readRoles = { Role.ROLE_ROOT.getSecurityName(), Role.ROLE_USER.getSecurityName() };

        private String[] writeRoles = { Role.ROLE_ROOT.getSecurityName(), Role.ROLE_SURVEY_USER.getSecurityName(),
                Role.ROLE_APP_ADMIN.getSecurityName(), Role.ROLE_HUB_ADMIN.getSecurityName() };

        @Override
        public String[] getUrls() {
            return urls;
        }

        @Override
        public String[] getReadRoles() {
            return readRoles;
        }

        @Override
        public String[] getWriteRoles() {
            return writeRoles;
        }
    }

}
