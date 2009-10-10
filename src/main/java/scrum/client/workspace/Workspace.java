package scrum.client.workspace;

import ilarkesto.gwt.client.AWidget;
import ilarkesto.gwt.client.FullscreenPanel;
import ilarkesto.gwt.client.Gwt;
import ilarkesto.gwt.client.GwtLogger;
import ilarkesto.gwt.client.LockWidget;
import ilarkesto.gwt.client.SwitcherWidget;
import scrum.client.ComponentManager;
import scrum.client.context.UiComponent;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class Workspace extends AWidget {

	public static final int HEADER_HEIGHT = 25;

	private LockWidget locker;
	private LockInfoWidget lockInfo;
	private SwitcherWidget sidebar;
	private SwitcherWidget workarea;

	@Override
	protected Widget onInitialization() {
		setHeight100();

		lockInfo = new LockInfoWidget();
		HeaderWidget header = new HeaderWidget();
		sidebar = new SwitcherWidget(true);
		workarea = new SwitcherWidget(true);

		ScrollPanel sidebarScroller = new ScrollPanel(sidebar);
		sidebarScroller.setHeight("100%");

		ScrollPanel workareaScroller = new ScrollPanel(workarea);
		workareaScroller.setHeight("100%");

		FlowPanel body = new FlowPanel();
		body.setStyleName("Workspace-body");
		body.add(Gwt.createDiv("Workspace-body-west", sidebarScroller));
		body.add(Gwt.createDiv("Workspace-body-center", workareaScroller));

		FlowPanel workspace = Gwt.createFlowPanel(Gwt.createDiv("Workspace-header", header), body);
		workspace.setStyleName("Workspace");

		locker = new LockWidget(workspace);

		return new FullscreenPanel(locker);
	}

	public void activateStartView() {
		activateView(ComponentManager.get().getHomeContext());
	}

	public void activateProjectView() {
		activateView(ComponentManager.get().getProjectContext());
	}

	private void activateView(UiComponent view) {
		GwtLogger.DEBUG("Activating view:", view);
		sidebar.show(view.getSidebarWidget());
		workarea.show(view.getWorkareaWidget());
		unlock();
	}

	public void abort(String message) {
		GwtLogger.DEBUG("Locking UI for ABORT:", message);
		lockInfo.showBug(message);
		locker.lock(lockInfo);
	}

	public void lock(String message) {
		initialize();
		GwtLogger.DEBUG("Locking UI:", message);
		lockInfo.showWait(message);
		locker.lock(lockInfo);
	}

	public void unlock() {
		GwtLogger.DEBUG("Unlocking UI");
		locker.unlock();
	}

	public void showError(String message) {
		final DialogBox db = new DialogBox();
		db.setSize("200", "150");
		db.setPopupPosition(100, 100);

		FlowPanel panel = new FlowPanel();
		Label text = new Label(message);
		panel.add(text);

		Button close = new Button("close");
		close.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				db.hide();
			}
		});
		panel.add(close);

		db.add(panel);
		db.show();
	}

	public SwitcherWidget getWorkarea() {
		return workarea;
	}

	public SwitcherWidget getSidebar() {
		return sidebar;
	}

}