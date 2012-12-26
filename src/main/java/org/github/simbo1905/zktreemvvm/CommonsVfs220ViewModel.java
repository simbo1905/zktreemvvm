package org.github.simbo1905.zktreemvvm;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.zkoss.zul.TreeModel;

public class CommonsVfs220ViewModel {
	/*
	 * could be something like "file:///tmp/" but that would be a security risk
	 */
	private static final String FILE_SYSTEM_URI = "jar:http://repo1.maven.org/maven2/org/apache/commons/commons-vfs2/2.0/commons-vfs2-2.0.jar";
	
	TreeModel<FileObject> _model;
	public TreeModel<FileObject> getModel() {
		if (_model == null) {
			try {
				FileSystemManager fsManager = VFS.getManager();
				FileObject jarFileObject = fsManager.resolveFile( FILE_SYSTEM_URI );
				_model = new CachingVfsTreeModel(jarFileObject);
			} catch (FileSystemException e) {
				throw new IllegalArgumentException(String.format("Could not open VFS remote jar uri: %s",FILE_SYSTEM_URI),e);
			}
			}
		return _model;
	}
	
	private FileObject pickedItem = null;

	public FileObject getPickedItem() {
		return pickedItem;
	}

	public void setPickedItem(FileObject pickedItem) {
		this.pickedItem = pickedItem;
	}
}
