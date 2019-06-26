package com.ise_web.eclipse.plugin.findIBatisXml.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.search.internal.ui.text.FileSearchQuery;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.FileTextSearchScope;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {

    /**
     * The constructor.
     */
    public SampleHandler() {
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        // 現在アクティブなエディターを取得
        IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

        // 選択されているテキストの文字列を取得する。
        String text = "";
        if (editorPart instanceof ITextEditor) {
            ISelectionProvider selectionProvider = ((ITextEditor)editorPart).getSelectionProvider();
            ISelection selection = selectionProvider.getSelection();
            ITextSelection textSelection = (ITextSelection)selection;
            text = textSelection.getText();
        }

        // 見選択の場合は、メッセージを表示する。
        if (text == null || "".equals(text)) {
            IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
            MessageDialog.openInformation(
                    window.getShell(),
                    "メッセージ",
                    String.valueOf("検索対象を選択して下さい"));
            return null;
        }

        IEditorInput editorInput = editorPart.getEditorInput();

        // 選択された文字列のあるファイルからプロジェクトを取得
        IProject project = ((IFileEditorInput) editorInput).getFile().getProject();

        // プロジェクトとフェーズ名から対象SQLのフォルダを取得
        IFolder folder = project.getFolder("src/");

        // 検索スコープの生成
        FileTextSearchScope scope = FileTextSearchScope.newSearchScope(
                new IResource[] { folder }, new String[] { "*.xml" }, false);

        // クエリを生成
        ISearchQuery query = new FileSearchQuery(text, false, false, scope);

        // バックグランドで検索を実行
        NewSearchUI.runQueryInBackground(query);

//        // ファイルを開く
//        IWorkbench workbench = PlatformUI.getWorkbench();
//        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
//        IWorkbenchPage page = window.getActivePage();
//        IEditorPart editorPart = page.openEditor(file);

        return null;
    }

}
