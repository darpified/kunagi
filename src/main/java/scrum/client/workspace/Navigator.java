/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package scrum.client.workspace;

import ilarkesto.core.scope.Scope;
import ilarkesto.gwt.client.AGwtEntity;
import scrum.client.ScrumGwt;
import scrum.client.ScrumScopeManager;
import scrum.client.admin.User;
import scrum.client.collaboration.ForumSupport;
import scrum.client.communication.TouchLastActivityServiceCall;
import scrum.client.core.ApplicationStartedEvent;
import scrum.client.core.ApplicationStartedHandler;
import scrum.client.project.Project;
import scrum.client.project.ProjectDataReceivedEvent;
import scrum.client.project.SelectProjectServiceCall;
import scrum.client.search.SearchResultsWidget;
import scrum.client.workspace.history.HistoryToken;
import scrum.client.workspace.history.HistoryTokenObserver;

import com.google.gwt.user.client.ui.Widget;

public class Navigator extends GNavigator implements BlockExpandedHandler, ApplicationStartedHandler,
		HistoryTokenObserver {

	public static enum Mode {
		USER, PROJECT
	}

	private HistoryToken historyToken;

	private Mode currentMode;

	@Override
	public void initialize() {
		historyToken = new HistoryToken(this);
	}

	@Override
	public void onProjectChanged() {
		String projectId = historyToken.getProjectId();
		if (projectId == null) {
			showUserMode(historyToken.getPage());
		} else {
			showProject(projectId, historyToken.getPage(), historyToken.getEntityId());
		}
	}

	@Override
	public void onPageOrEntityChanged() {
		showPageAndEntity();
	}

	@Override
	public void onApplicationStarted(ApplicationStartedEvent event) {
		historyToken.evalHistoryToken();
		if (!historyToken.isProjectIdSet()) {
			gotoUsersStart();
		}
	}

	private void gotoUsersStart() {
		User user = auth.getUser();
		Project project = user.getCurrentProject();
		if (project == null || user.isAdmin()) {
			gotoProjectSelector();
		} else {
			gotoProject(project.getId());
		}
	}

	public void gotoProjectSelector() {
		historyToken.update(null);
	}

	public void gotoProject(String projectId) {
		historyToken.update(projectId);
	}

	public void gotoCurrentProjectSearch() {
		historyToken.updatePage(Page.getPageName(SearchResultsWidget.class));
	}

	private void showProject(String projectId, String page, String entityId) {
		Project project = Scope.get().getComponent(Project.class);
		if (project != null && !projectId.equals(project.getId())) {
			project = null;
		}

		if (project == null) {
			project = dao.getProject(projectId);
			if (project == null) throw new RuntimeException("Project does not exist: " + projectId);
			acitvateProjectMode(project, page, entityId);
			return;
		}

		showPageAndEntity(page, entityId);
	}

	private void showPageAndEntity() {
		showPageAndEntity(historyToken.getPage(), historyToken.getEntityId());
	}

	private void showPageAndEntity(String page, String entityId) {
		new TouchLastActivityServiceCall().execute();
		ProjectWorkspaceWidgets workspace = Scope.get().getComponent(ProjectWorkspaceWidgets.class);

		if (historyToken.getPage() == null && page == null) page = HistoryToken.START_PAGE;

		if (page != null) workspace.showPage(page);

		if (entityId != null) {
			if (ScrumGwt.isEntityReferenceOrWikiPage(entityId)) {
				workspace.showEntityByReference(entityId);
			} else {
				if ("Forum".equals(historyToken.getPage())) {
					ForumSupport entity = (ForumSupport) dao.getEntity(entityId);
					workspace.showForum(entity);
				} else {
					workspace.showEntityById(entityId);
				}
			}
		}
	}

	@Override
	public void onBlockExpanded(BlockExpandedEvent event) {
		// Object object = event.getObject();
		// if (object instanceof AGwtEntity) {
		// setToken((AGwtEntity) object);
		// }
	}

	private void showUserMode(String page) {
		if (currentMode == Mode.PROJECT) {
			ScrumScopeManager.destroyProjectScope();
		}

		log.info("Activating USER mode");
		Scope.get().getComponent(UsersWorkspaceWidgets.class).activate(page);
		currentMode = Mode.USER;
	}

	private void acitvateProjectMode(final Project project, final String page, final String entityId) {
		assert project != null;

		if (currentMode == Mode.PROJECT) ScrumScopeManager.destroyProjectScope();

		log.info("Activating PROJECT mode");
		Scope.get().getComponent(Ui.class).lock("Loading " + project.getLabel() + "...");
		new SelectProjectServiceCall(project.getId()).execute(new Runnable() {

			@Override
			public void run() {
				ScrumScopeManager.createProjectScope(project);
				currentMode = Mode.PROJECT;
				new ProjectDataReceivedEvent().fireInCurrentScope();
				showPageAndEntity(page, entityId);
			}
		});
	}

	public static String getPageHref(String page) {
		return '#' + getPageHistoryToken(page);
	}

	public static String getPageHistoryToken(String page) {
		StringBuilder sb = new StringBuilder();
		Project project = Scope.get().getComponent(Project.class);
		if (project != null) sb.append("project=").append(project.getId()).append("|");
		sb.append("page=").append(page);
		return sb.toString();
	}

	public static String getPageHref(Class<? extends Widget> pageClass) {
		return getPageHref(Page.getPageName(pageClass));
	}

	public static String getEntityHistoryToken(AGwtEntity entity) {
		String id = entity.getId();
		return getEntityHistoryToken(id);
	}

	public static String getEntityHref(AGwtEntity entity) {
		String id = entity.getId();
		return getEntityHref(id);
	}

	public static String getEntityHref(String entityId) {
		return '#' + getEntityHistoryToken(entityId);
	}

	public static String getEntityHistoryToken(String entityId) {
		return getEntityHistoryToken(null, entityId);
	}

	public static String getEntityHistoryToken(String page, String entityId) {
		StringBuilder sb = new StringBuilder();

		Project project = Scope.get().getComponent(Project.class);
		if (project != null) sb.append("project=").append(project.getId()).append("|");

		Navigator navigator = Scope.get().getComponent(Navigator.class);
		if (page == null && navigator != null) {
			page = navigator.historyToken.getPage();
		}
		if (page != null) sb.append("page=").append(page).append("|");

		sb.append("entity=").append(entityId);
		return sb.toString();
	}

	public boolean isToggleMode() {
		return historyToken.isToggle();
	}

}