package org.github.simbo1905.zktreemvvm;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.zkoss.zul.AbstractTreeModel;

public class VfsTreeModel extends AbstractTreeModel<FileObject> {

	private static final long serialVersionUID = -8137575824288195035L;

	public VfsTreeModel(FileObject root){
		super(root);
	}
	
	@Override
	public FileObject getChild(FileObject parent, int index) {
		FileObject child = null;
		try {
			FileObject[] children = parent.getChildren();
			child = children[index];
		} catch (FileSystemException e) {
			throw new IllegalArgumentException(e);
		}
		return child;
	}

	@Override
	public int getChildCount(FileObject node) {
		int childCount = 0;
		try {
			FileType type = node.getType();
			if( type == FileType.FOLDER ){
				childCount = node.getChildren().length;
			}
		} catch (FileSystemException e) {
			throw new IllegalArgumentException(e);
		}
		return childCount;
	}

	@Override
	public boolean isLeaf(FileObject node) {
		boolean isLeaf = false;
		try {
			FileType type = node.getType();
			isLeaf = (type == FileType.FILE );
		} catch (FileSystemException e) {
			throw new IllegalArgumentException(e);
		}
		return isLeaf;
	}

}
