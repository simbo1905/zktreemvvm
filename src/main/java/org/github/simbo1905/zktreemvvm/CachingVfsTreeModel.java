package org.github.simbo1905.zktreemvvm;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;

public class CachingVfsTreeModel extends VfsTreeModel {

	private static final long serialVersionUID = 6277408689433600782L;

	public CachingVfsTreeModel(FileObject root) {
		super(root);
	}	
	
	Map<FileObjectIndexTuple,FileObject> childCache = new HashMap<FileObjectIndexTuple,FileObject>();
	
	static class FileObjectIndexTuple {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fo == null) ? 0 : fo.hashCode());
			result = prime * result + index;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FileObjectIndexTuple other = (FileObjectIndexTuple) obj;
			if (fo == null) {
				if (other.fo != null)
					return false;
			} else if (!fo.equals(other.fo))
				return false;
			if (index != other.index)
				return false;
			return true;
		}
		final FileObject fo;
		final int index;
		FileObjectIndexTuple(FileObject fo, int index){
			this.fo = fo;
			this.index = index;
		}
		static FileObjectIndexTuple of(FileObject fo, int index){
			return new FileObjectIndexTuple(fo, index);
		}
	}
	
	
	@Override
	public FileObject getChild(FileObject parent, int index) {
		FileObjectIndexTuple tuple = FileObjectIndexTuple.of(parent, index);
		FileObject child = childCache.get(tuple);
		if( null == child ){
			child = super.getChild(parent, index);
			childCache.put(tuple, child);
		}
		return child;
	}

	Map<FileObject,Integer> childCountCache = new HashMap<FileObject,Integer>();
	
	@Override
	public int getChildCount(FileObject node) {
		Integer count = childCountCache.get(node);
		if( count == null){
			count = super.getChildCount(node);
			childCountCache.put(node, count);
		}
		return count;
	}

	Map<FileObject,Boolean> isLeafCache = new HashMap<FileObject,Boolean>();
	
	@Override
	public boolean isLeaf(FileObject node) {
		Boolean isLeaf = isLeafCache.get(node);
		if( null == isLeaf ){
			isLeaf = super.isLeaf(node);
			isLeafCache.put(node, isLeaf);
		}
		return isLeaf; 
	}
	
	Map<FileObject,int[]> pathCache = new HashMap<FileObject,int[]>();

	@Override
	public int[] getPath(FileObject node) {
		int[] paths = pathCache.get(node);
		if( paths == null ){
			paths = super.getPath(node);
			pathCache.put(node, paths);
		}
		return paths;
	}
}
