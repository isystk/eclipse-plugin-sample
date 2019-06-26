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

        // ���݃A�N�e�B�u�ȃG�f�B�^�[���擾
        IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

        // �I������Ă���e�L�X�g�̕�������擾����B
        String text = "";
        if (editorPart instanceof ITextEditor) {
            ISelectionProvider selectionProvider = ((ITextEditor)editorPart).getSelectionProvider();
            ISelection selection = selectionProvider.getSelection();
            ITextSelection textSelection = (ITextSelection)selection;
            text = textSelection.getText();
        }

        // ���I���̏ꍇ�́A���b�Z�[�W��\������B
        if (text == null || "".equals(text)) {
            IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
            MessageDialog.openInformation(
                    window.getShell(),
                    "���b�Z�[�W",
                    String.valueOf("�����Ώۂ�I�����ĉ�����"));
            return null;
        }

        IEditorInput editorInput = editorPart.getEditorInput();

        // �I�����ꂽ������̂���t�@�C������v���W�F�N�g���擾
        IProject project = ((IFileEditorInput) editorInput).getFile().getProject();

        // �v���W�F�N�g�ƃt�F�[�Y������Ώ�SQL�̃t�H���_���擾
        IFolder folder = project.getFolder("src/");

        // �����X�R�[�v�̐���
        FileTextSearchScope scope = FileTextSearchScope.newSearchScope(
                new IResource[] { folder }, new String[] { "*.xml" }, false);

        // �N�G���𐶐�
        ISearchQuery query = new FileSearchQuery(text, false, false, scope);

        // �o�b�N�O�����h�Ō��������s
        NewSearchUI.runQueryInBackground(query);

//        // �t�@�C�����J��
//        IWorkbench workbench = PlatformUI.getWorkbench();
//        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
//        IWorkbenchPage page = window.getActivePage();
//        IEditorPart editorPart = page.openEditor(file);

        return null;
    }

}
