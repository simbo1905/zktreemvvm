package org.github.simbo1905.zktreemvvm;

import static org.hamcrest.CoreMatchers.is;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VfsModelTest {
	FileSystemManager fsManager = null;
	FileObject zipFileObject = null;
	
	@Before
	public void setup() throws Exception{
		fsManager = VFS.getManager();
		zipFileObject = fsManager.resolveFile( "jar:http://repo1.maven.org/maven2/org/apache/commons/commons-vfs2/2.0/commons-vfs2-2.0.jar" );
	}
	
	@Test
	public void testVfsTreeModelNoneLeafNode() throws Exception {
		// given
		VfsTreeModel model = new VfsTreeModel(zipFileObject);
		// when
		boolean isLeaf = model.isLeaf(zipFileObject);
		// then
		Assert.assertFalse(isLeaf);
	}
	
	@Test
	public void testVfsTreeModelLeafNode() throws Exception { 
		// given
		VfsTreeModel model = new VfsTreeModel(zipFileObject);
		FileObject manifest = zipFileObject.getChild("META-INF").getChild("MANIFEST.MF");
		// when
		boolean isLeaf = model.isLeaf(manifest);
		// then
		Assert.assertTrue(isLeaf);
	}
	
	@Test
	public void testVsfTreeModelGetChildCount() throws Exception {
		// given
		VfsTreeModel model = new VfsTreeModel(zipFileObject);
		// when
		int count = model.getChildCount(zipFileObject);
		// then
		Assert.assertThat(count,is(2));
	}
	
	@Test
	public void testVsfTreeModelGetChild() throws Exception {
		// given
		VfsTreeModel model = new VfsTreeModel(zipFileObject);
		// when
		FileObject child = model.getChild(zipFileObject, 1);
		// then
		Assert.assertThat(child.getName().getBaseName(),is("META-INF"));
	}

}
