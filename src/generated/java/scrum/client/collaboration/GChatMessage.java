// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.gen.GwtEntityGenerator










package scrum.client.collaboration;

import java.util.*;
import ilarkesto.persistence.*;
import ilarkesto.logging.*;
import ilarkesto.base.*;
import ilarkesto.base.time.*;
import ilarkesto.auth.*;
import scrum.client.common.*;
import ilarkesto.gwt.client.*;

public abstract class GChatMessage
            extends ilarkesto.gwt.client.AGwtEntity {

    protected scrum.client.Dao getDao() {
        return scrum.client.Dao.get();
    }

    public GChatMessage() {
    }

    public GChatMessage(Map data) {
        super(data);
        updateProperties(data);
    }

    public static final String ENTITY_TYPE = "chatMessage";

    @Override
    public final String getEntityType() {
        return ENTITY_TYPE;
    }

    // --- project ---

    private String projectId;

    public final scrum.client.project.Project getProject() {
        if (projectId == null) return null;
        return getDao().getProject(this.projectId);
    }

    public final boolean isProjectSet() {
        return projectId != null;
    }

    public final ChatMessage setProject(scrum.client.project.Project project) {
        String id = project == null ? null : project.getId();
        if (equals(this.projectId, id)) return (ChatMessage) this;
        this.projectId = id;
        propertyChanged("projectId", this.projectId);
        return (ChatMessage)this;
    }

    public final boolean isProject(scrum.client.project.Project project) {
        return equals(this.projectId, project);
    }

    // --- author ---

    private String authorId;

    public final scrum.client.admin.User getAuthor() {
        if (authorId == null) return null;
        return getDao().getUser(this.authorId);
    }

    public final boolean isAuthorSet() {
        return authorId != null;
    }

    public final ChatMessage setAuthor(scrum.client.admin.User author) {
        String id = author == null ? null : author.getId();
        if (equals(this.authorId, id)) return (ChatMessage) this;
        this.authorId = id;
        propertyChanged("authorId", this.authorId);
        return (ChatMessage)this;
    }

    public final boolean isAuthor(scrum.client.admin.User author) {
        return equals(this.authorId, author);
    }

    // --- text ---

    private java.lang.String text ;

    public final java.lang.String getText() {
        return this.text ;
    }

    public final ChatMessage setText(java.lang.String text) {
        this.text = text ;
        propertyChanged("text", this.text);
        return (ChatMessage)this;
    }

    public final boolean isText(java.lang.String text) {
        return equals(this.text, text);
    }

    // --- update properties by map ---

    public void updateProperties(Map props) {
        projectId = (String) props.get("projectId");
        authorId = (String) props.get("authorId");
        text  = (java.lang.String) props.get("text");
    }

    @Override
    public void storeProperties(Map properties) {
        super.storeProperties(properties);
        properties.put("projectId", this.projectId);
        properties.put("authorId", this.authorId);
        properties.put("text", this.text);
    }

}