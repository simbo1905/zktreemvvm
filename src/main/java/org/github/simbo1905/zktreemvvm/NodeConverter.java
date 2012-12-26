package org.github.simbo1905.zktreemvvm;

import org.apache.commons.vfs2.FileObject;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zul.Treecell;

/**
 * A class to render a friendly screen name for a FileObject
 */
public class NodeConverter implements Converter<String,FileObject,Treecell> {

	@Override
	public String coerceToUi(FileObject beanProp, Treecell component,
			BindContext ctx) {
		return beanProp.getName().getPath()+"/"+beanProp.getName().getBaseName();
	}

	@Override
	public FileObject coerceToBean(String compAttr, Treecell component,
			BindContext ctx) {
		throw new AssertionError("not implemented");
	}
}
