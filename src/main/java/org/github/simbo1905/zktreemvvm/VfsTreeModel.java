package org.github.simbo1905.zktreemvvm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.AbstractTreeModel;

public class VfsTreeModel extends AbstractTreeModel<FileObject> {

	final private static Logger log = LoggerFactory.getLogger(VfsTreeModel.class);
	
	private static final long serialVersionUID = -8137575824288195035L;

	public VfsTreeModel(FileObject root){
		super(root);
	}
	
	String innerName(FileObject jarFile){
		String name = jarFile.toString();
		return name.substring(name.indexOf('!'));
	}
	
	int level(FileObject jarFile){
		String name = innerName(jarFile);
		return name.replaceAll("[^/]", "").length();
	}
	
	@Override
	public FileObject getChild(FileObject parent, int index) {
		log.info(String.format("%s getChild on %s with index %s", level(parent), innerName(parent), index));
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
		log.info(String.format("%s getChildCount on %s returning %s",level(node),innerName(node), childCount));
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
		log.info(String.format("%s isLeaf on %s returning %s", level(node),innerName(node), isLeaf));
		return isLeaf;
	}

	@Override
	public int[] getPath(FileObject node) {
		List<Integer> paths = new ArrayList<Integer>();
		try {
			FileObject parent = node.getParent();
			while (parent != null && parent.getType().equals(FileType.FOLDER)) {
				FileObject[] children = parent.getChildren();
				for( int index = 0; index < children.length; index++){
					FileObject c = children[index];
					if( node.equals(c)){
						paths.add(index);
						break;
					}
				}
				node = parent;
				parent = node.getParent();
			}
		} catch (FileSystemException e) {
			throw new IllegalArgumentException(e);
		}
		int[] p = new int[paths.size()];
		for( int index = 0; index < paths.size(); index++){
			p[index] = paths.get(p.length - 1 - index); // reverse
		}
		//out.println(String.format("%s getPath on %s",level(node),innerName(node)));
		return p;
	}
}
