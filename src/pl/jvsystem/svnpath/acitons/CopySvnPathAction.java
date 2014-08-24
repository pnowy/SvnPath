package pl.jvsystem.svnpath.acitons;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.idea.svn.SvnUtil;
import org.jetbrains.idea.svn.SvnVcs;
import org.tmatesoft.svn.core.SVNURL;


import java.awt.datatransfer.StringSelection;
import java.io.File;

/**
 * Action responsible for copying the SNV remote URL.
 */
public class CopySvnPathAction extends AnAction {

	@Override
	public void actionPerformed(AnActionEvent e) {
		final Project project = e.getProject();

		final StatusBar statusBar = WindowManager.getInstance().getStatusBar(DataKeys.PROJECT.getData(e.getDataContext()));
		final VirtualFile virtualFile = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
		if (statusBar == null || virtualFile == null) {
			return;
		}

		SVNURL url = SvnUtil.getUrl(SvnVcs.getInstance(project), new File(virtualFile.getPath()));
		if (url != null) {
			String ulrString = url.toString();
			CopyPasteManager.getInstance().setContents(new StringSelection(ulrString));
			JBPopupFactory.getInstance()
					.createHtmlTextBalloonBuilder(String.format("The URL: %s has been copied correctly!", ulrString), MessageType.INFO, null)
					.setFadeoutTime(7500)
					.createBalloon()
					.show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
		} else {
			JBPopupFactory.getInstance()
					.createHtmlTextBalloonBuilder("The error occurred when the URL was copied!", MessageType.ERROR, null)
					.setFadeoutTime(7500)
					.createBalloon()
					.show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
		}
	}
}
