package org.github.simbo1905.zktreemvvm.fiddle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @see http://zkfiddle.org/sample/3dm77ch/1-MVVM-tree#source-4
 */
public class FooNode {
	private FooNode _parent;
	private List<FooNode> _children;
	private int _index;
	private String _label = "";
	public FooNode (FooNode parent, int index, String label) {
		_parent = parent;
		_index = index;
		_label = label;
	}
	public void setParent (FooNode parent) {
		_parent = parent;
	}
	public FooNode getParent () {
		return _parent;
	}

	public void appendChild (FooNode child) {
		if (_children == null)
			_children = new ArrayList<FooNode>();
		_children.add(child);
	}
	public List<FooNode> getChildren () {
		if (_children == null)
			return Collections.emptyList();
		return _children;
	}
	public void setIndex (int index) {
		_index = index;
	}
	public int getIndex () {
		return _index;
	}
	public String getLabel () {
		return _label;
	}
	public String toString () {
		return getLabel();
	}
}