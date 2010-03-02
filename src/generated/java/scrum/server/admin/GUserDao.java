// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.legacy.generator.DaoGenerator










package scrum.server.admin;

import java.util.*;
import ilarkesto.persistence.*;
import ilarkesto.core.logging.Log;
import ilarkesto.base.*;
import ilarkesto.base.time.*;
import ilarkesto.auth.*;
import ilarkesto.fp.*;

public abstract class GUserDao
            extends ilarkesto.auth.AUserDao<User> {

    public final String getEntityName() {
        return User.TYPE;
    }

    public final Class getEntityClass() {
        return User.class;
    }

    public Set<User> getEntitiesVisibleForUser(final scrum.server.admin.User user) {
        return getEntities(new Predicate<User>() {
            public boolean test(User e) {
                return Auth.isVisible(e, user);
            }
        });
    }

    // --- clear caches ---
    public void clearCaches() {
        usersByNameCache.clear();
        namesCache = null;
        usersByAdminCache.clear();
        usersByEmailCache.clear();
        emailsCache = null;
        usersByCurrentProjectCache.clear();
        currentProjectsCache = null;
        usersByColorCache.clear();
        colorsCache = null;
    }

    @Override
    public void entityDeleted(EntityEvent event) {
        super.entityDeleted(event);
        if (event.getEntity() instanceof User) {
            clearCaches();
        }
    }

    @Override
    public void entitySaved(EntityEvent event) {
        super.entitySaved(event);
        if (event.getEntity() instanceof User) {
            clearCaches();
        }
    }

    // -----------------------------------------------------------
    // - name
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<User>> usersByNameCache = new Cache<java.lang.String,Set<User>>(
            new Cache.Factory<java.lang.String,Set<User>>() {
                public Set<User> create(java.lang.String name) {
                    return getEntities(new IsName(name));
                }
            });

    public final Set<User> getUsersByName(java.lang.String name) {
        return usersByNameCache.get(name);
    }
    private Set<java.lang.String> namesCache;

    public final Set<java.lang.String> getNames() {
        if (namesCache == null) {
            namesCache = new HashSet<java.lang.String>();
            for (User e : getEntities()) {
                if (e.isNameSet()) namesCache.add(e.getName());
            }
        }
        return namesCache;
    }

    private static class IsName implements Predicate<User> {

        private java.lang.String value;

        public IsName(java.lang.String value) {
            this.value = value;
        }

        public boolean test(User e) {
            return e.isName(value);
        }

    }

    // -----------------------------------------------------------
    // - admin
    // -----------------------------------------------------------

    private final Cache<Boolean,Set<User>> usersByAdminCache = new Cache<Boolean,Set<User>>(
            new Cache.Factory<Boolean,Set<User>>() {
                public Set<User> create(Boolean admin) {
                    return getEntities(new IsAdmin(admin));
                }
            });

    public final Set<User> getUsersByAdmin(boolean admin) {
        return usersByAdminCache.get(admin);
    }

    private static class IsAdmin implements Predicate<User> {

        private boolean value;

        public IsAdmin(boolean value) {
            this.value = value;
        }

        public boolean test(User e) {
            return value == e.isAdmin();
        }

    }

    // -----------------------------------------------------------
    // - email
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<User>> usersByEmailCache = new Cache<java.lang.String,Set<User>>(
            new Cache.Factory<java.lang.String,Set<User>>() {
                public Set<User> create(java.lang.String email) {
                    return getEntities(new IsEmail(email));
                }
            });

    public final Set<User> getUsersByEmail(java.lang.String email) {
        return usersByEmailCache.get(email);
    }
    private Set<java.lang.String> emailsCache;

    public final Set<java.lang.String> getEmails() {
        if (emailsCache == null) {
            emailsCache = new HashSet<java.lang.String>();
            for (User e : getEntities()) {
                if (e.isEmailSet()) emailsCache.add(e.getEmail());
            }
        }
        return emailsCache;
    }

    private static class IsEmail implements Predicate<User> {

        private java.lang.String value;

        public IsEmail(java.lang.String value) {
            this.value = value;
        }

        public boolean test(User e) {
            return e.isEmail(value);
        }

    }

    // -----------------------------------------------------------
    // - currentProject
    // -----------------------------------------------------------

    private final Cache<scrum.server.project.Project,Set<User>> usersByCurrentProjectCache = new Cache<scrum.server.project.Project,Set<User>>(
            new Cache.Factory<scrum.server.project.Project,Set<User>>() {
                public Set<User> create(scrum.server.project.Project currentProject) {
                    return getEntities(new IsCurrentProject(currentProject));
                }
            });

    public final Set<User> getUsersByCurrentProject(scrum.server.project.Project currentProject) {
        return usersByCurrentProjectCache.get(currentProject);
    }
    private Set<scrum.server.project.Project> currentProjectsCache;

    public final Set<scrum.server.project.Project> getCurrentProjects() {
        if (currentProjectsCache == null) {
            currentProjectsCache = new HashSet<scrum.server.project.Project>();
            for (User e : getEntities()) {
                if (e.isCurrentProjectSet()) currentProjectsCache.add(e.getCurrentProject());
            }
        }
        return currentProjectsCache;
    }

    private static class IsCurrentProject implements Predicate<User> {

        private scrum.server.project.Project value;

        public IsCurrentProject(scrum.server.project.Project value) {
            this.value = value;
        }

        public boolean test(User e) {
            return e.isCurrentProject(value);
        }

    }

    // -----------------------------------------------------------
    // - color
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<User>> usersByColorCache = new Cache<java.lang.String,Set<User>>(
            new Cache.Factory<java.lang.String,Set<User>>() {
                public Set<User> create(java.lang.String color) {
                    return getEntities(new IsColor(color));
                }
            });

    public final Set<User> getUsersByColor(java.lang.String color) {
        return usersByColorCache.get(color);
    }
    private Set<java.lang.String> colorsCache;

    public final Set<java.lang.String> getColors() {
        if (colorsCache == null) {
            colorsCache = new HashSet<java.lang.String>();
            for (User e : getEntities()) {
                if (e.isColorSet()) colorsCache.add(e.getColor());
            }
        }
        return colorsCache;
    }

    private static class IsColor implements Predicate<User> {

        private java.lang.String value;

        public IsColor(java.lang.String value) {
            this.value = value;
        }

        public boolean test(User e) {
            return e.isColor(value);
        }

    }

    // --- valueObject classes ---
    @Override
    protected Set<Class> getValueObjectClasses() {
        Set<Class> ret = new HashSet<Class>(super.getValueObjectClasses());
        return ret;
    }

    @Override
    public Map<String, Class> getAliases() {
        Map<String, Class> aliases = new HashMap<String, Class>(super.getAliases());
        return aliases;
    }

    // --- dependencies ---

    scrum.server.project.ProjectDao projectDao;

    public void setProjectDao(scrum.server.project.ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

}