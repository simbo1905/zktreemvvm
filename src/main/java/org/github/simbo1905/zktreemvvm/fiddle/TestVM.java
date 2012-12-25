package org.github.simbo1905.zktreemvvm.fiddle;

import org.zkoss.zul.TreeModel;

/**
 * @see http://zkfiddle.org/sample/3dm77ch/1-MVVM-tree#source-4
 */
public class TestVM {
	TreeModel<FooNode> _model;
	public TreeModel<FooNode> getModel() {
		if (_model == null) {
			_model = new FooModel(getFooRoot());
		}
		return _model;
	}
	// create a FooNodes tree structure and return the root
	private FooNode getFooRoot () {
		FooNode root = new FooNode(null, 0, "");
		for (int i = 0; i < 10; i++) {
			FooNode firstLevelNode = new FooNode(root, i, "first - " + i);
			for (int j = 0; j <= i; j++) {
				FooNode secondLevelNode = new FooNode(firstLevelNode, j, "second - " + j);
				firstLevelNode.appendChild(secondLevelNode);
			}
			root.appendChild(firstLevelNode);
		}
		return root;
	}
}
