package org.github.simbo1905.zktreemvvm.fiddle;

import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

/**
 * @see http://zkfiddle.org/sample/3dm77ch/1-MVVM-tree#source-4
 */
public class FooModel extends AbstractTreeModel<FooNode> {
	private static final long serialVersionUID = 1L;
	public FooModel(FooNode root) {
        super(root);
    }
    public boolean isLeaf(FooNode node) {
        return ((FooNode)node).getChildren().size() == 0; //at most 4 levels
    }
    public FooNode getChild(FooNode parent, int index) {
        return ((FooNode)parent).getChildren().get(index);
    }
    public int getChildCount(FooNode parent) {
        return ((FooNode)parent).getChildren().size(); //each node has 5 children
    }
    public int getIndexOfChild(FooNode parent, FooNode child) {
    	List<FooNode> children = ((FooNode)parent).getChildren();
    	for (int i = 0; i < children.size(); i++) {
    		if (children.get(i).equals(children))
    			return i;
    	}
        return -1;
    }
};
