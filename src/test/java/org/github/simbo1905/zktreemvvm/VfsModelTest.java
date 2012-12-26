package org.github.simbo1905.zktreemvvm;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
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

	@Test
	public void testVsfTreeeModelGetPath() throws Exception {
		//jar:http://repo1.maven.org/maven2/org/apache/commons/commons-vfs2/2.0/commons-vfs2-2.0.jar!/org/apache/commons/vfs2/provider/local 0,0,0,0,31,54,
		//given
		VfsTreeModel model = new VfsTreeModel(zipFileObject);
		FileObject child = zipFileObject.resolveFile("/org/apache/commons/vfs2/provider/local");
		// when
		int[] paths = model.getPath(child);
		// then
		int[] expected = new int[]{0,0,0,0,31,54};
		for( int index = 0; index < expected.length; index++){
			Assert.assertEquals(expected[index], paths[index]);
		}
	}
	
	@Test
	public void testCachingIsLeafNode() throws Exception { 
		// given
		List<FileObject> allBelowMetaInf = filesBelow(zipFileObject.getChild("META-INF"));
		VfsTreeModel rawModel = new VfsTreeModel(zipFileObject);
		CachingVfsTreeModel cacheModel = new CachingVfsTreeModel(zipFileObject);
		// when
		boolean[] modelValues = new boolean[allBelowMetaInf.size()];
		boolean[] cacheModelValues = new boolean[allBelowMetaInf.size()];
		boolean[] cacheRepeatModelValues = new boolean[allBelowMetaInf.size()];
		int index = 0;
		for( FileObject fo : allBelowMetaInf){
			boolean modelValue = rawModel.isLeaf(fo);
			modelValues[index] = modelValue;
			boolean cmodelValue = cacheModel.isLeaf(fo);
			cacheModelValues[index] = cmodelValue;
			boolean repeatModelValue = cacheModel.isLeaf(fo);
			cacheRepeatModelValues[index] = repeatModelValue;
			index++;
		}
		// then
		for( index = 0; index < modelValues.length; index++){
			Assert.assertEquals(modelValues[index], cacheModelValues[index]);
			Assert.assertEquals(modelValues[index], cacheRepeatModelValues[index]);
		}
	}
	
	@Test
	public void testCachingGetChild() throws Exception { 
		// given
		FileObject metaInf = zipFileObject.getChild("META-INF");
		VfsTreeModel rawModel = new VfsTreeModel(zipFileObject);
		CachingVfsTreeModel cacheModel = new CachingVfsTreeModel(zipFileObject);
		// when
		FileObject rawChild = rawModel.getChild(metaInf, 1);
		FileObject cacheChild = cacheModel.getChild(metaInf, 1);
		FileObject cacheRepeatChild = cacheModel.getChild(metaInf, 1);
		// then
		Assert.assertEquals(rawChild.getName().getURI(), cacheChild.getName().getURI());
		Assert.assertEquals(rawChild.getName().getURI(), cacheRepeatChild.getName().getURI());
	}

	@Test
	public void testGetChildCount() throws Exception {
		// given
		FileObject metaInf = zipFileObject.getChild("META-INF");
		VfsTreeModel rawModel = new VfsTreeModel(zipFileObject);
		CachingVfsTreeModel cacheModel = new CachingVfsTreeModel(zipFileObject);
		// when
		int rawCount = rawModel.getChildCount(metaInf);
		int cacheCount = cacheModel.getChildCount(metaInf);
		int repeatCount = cacheModel.getChildCount(metaInf);
		// then
		Assert.assertEquals(rawCount,cacheCount);
		Assert.assertEquals(rawCount,repeatCount);
	}
	
	@Test
	public void testCachedGetPath() throws Exception {
		//jar:http://repo1.maven.org/maven2/org/apache/commons/commons-vfs2/2.0/commons-vfs2-2.0.jar!/org/apache/commons/vfs2/provider/local 0,0,0,0,31,54,
		//given
		VfsTreeModel rawModel = new VfsTreeModel(zipFileObject);
		CachingVfsTreeModel cacheModel = new CachingVfsTreeModel(zipFileObject);
		FileObject child = zipFileObject.resolveFile("/org/apache/commons/vfs2/provider/local");
		// when
		int[] paths = rawModel.getPath(child);
		int[] cachedPaths = cacheModel.getPath(child);
		int[] repeatPaths = cacheModel.getPath(child);
		// then
		int[] expected = new int[]{0,0,0,0,31,54};
		for( int index = 0; index < expected.length; index++){
			Assert.assertEquals(expected[index], paths[index]);
			Assert.assertEquals(expected[index], cachedPaths[index]);
			Assert.assertEquals(expected[index], repeatPaths[index]);
		}		
	}
	
	private List<FileObject> filesBelow(FileObject manifest) throws FileSystemException {
		Deque<FileObject> stack = new ArrayDeque<FileObject>();
		List<FileObject> allChildren = new ArrayList<FileObject>();
		stack.push(manifest);
		while( !stack.isEmpty() ){
			FileObject next = stack.pop();
			allChildren.add(next);
			if( next.getType() == FileType.FOLDER){
				for( FileObject c : next.getChildren() ){
					stack.push(c);
				}
			}
		}
		return allChildren;
	}
	
	public static void main(String[] args) throws Exception {
		FileSystemManager fsManager = VFS.getManager();
		FileObject zipFileObject = fsManager.resolveFile( "jar:http://repo1.maven.org/maven2/org/apache/commons/commons-vfs2/2.0/commons-vfs2-2.0.jar" );
		FileObject child = zipFileObject.resolveFile("/org/apache/commons/vfs2/provider/local");
		
		VfsTreeModel model = new VfsTreeModel(zipFileObject);
		CachingVfsTreeModel cmodel = new CachingVfsTreeModel(zipFileObject);
		
		long[] normalTimes = new long[1000];
		long[] cacheTimes = new long[1000];
		
		for( int i = 0; i < normalTimes.length; i++ ){
			long normal = timeGetPath(child, model);	
			normalTimes[i] = normal;
			long cached = timeGetPath(child, cmodel);
			cacheTimes[i] = cached;
		}
		
		for( int i = 0; i < normalTimes.length; i++ ){
			if( i < 100 ) continue;
			long normal = normalTimes[i];
			long cached = cacheTimes[i];
			System.out.print(" "+(normal-cached));
		}

	}

	private static long timeGetPath(FileObject child, VfsTreeModel model) {
		long start = System.currentTimeMillis();
		model.getPath(child);
		long end = System.currentTimeMillis();
		long time = end - start;
		return time;
	}
}
