package org.github.simbo1905.zktreemvvm;

import org.apache.commons.vfs2.FileObject;

class FileObjectIndexTuple {
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