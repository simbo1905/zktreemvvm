package org.github.simbo1905.zktreemvvm;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.FileObject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Caching subclass of VfsTreeModel which naively caches calls for a minute. 
 * Note refreshing the page gets a new object so new caches. 
 * Note the caching strategy here is only a proof-of-concept. The timeout is 
 * on multiple related caches is unwise; it can lead to inconsistencies as  
 * separate caches refreshed at different times may not be an accurate or 
 * consistent snapshot of the underlying data. 
 */
public class CachingVfsTreeModel extends VfsTreeModel {

	private static final long serialVersionUID = 6277408689433600782L;

	public CachingVfsTreeModel(FileObject root) {
		super(root);
	}	
	
	LoadingCache<FileObjectIndexTuple,FileObject> childCache = CacheBuilder.newBuilder()
		       .expireAfterWrite(1, TimeUnit.MINUTES)
		       .build(
		           new CacheLoader<FileObjectIndexTuple,FileObject>() {
		             public FileObject load(FileObjectIndexTuple parentIndexTuple)  {
		            	 return CachingVfsTreeModel.super.getChild(parentIndexTuple.fo, parentIndexTuple.index);
		             }
		           });
	
	@Override
	public FileObject getChild(FileObject parent, int index) {
		FileObjectIndexTuple tuple = FileObjectIndexTuple.of(parent, index);
		FileObject child;
		try {
			child = childCache.get(tuple);
		} catch (ExecutionException e) {
			throw new IllegalArgumentException(e);
		}
		return child;
	}

	LoadingCache<FileObject, Integer> childCountCache = CacheBuilder.newBuilder()
		       .expireAfterWrite(1, TimeUnit.MINUTES)
		       .build(
		           new CacheLoader<FileObject, Integer>() {
		             public Integer load(FileObject node)  {
		            	 return CachingVfsTreeModel.super.getChildCount(node);
		             }
		           });
	
	@Override
	public int getChildCount(FileObject node) {
		Integer count;
		try {
			count = childCountCache.get(node);
		} catch (ExecutionException e) {
			throw new IllegalArgumentException(e);
		}
		return count;
	}

	LoadingCache<FileObject, Boolean> isLeafCache = CacheBuilder.newBuilder()
		       .expireAfterWrite(1, TimeUnit.MINUTES)
		       .build(
		           new CacheLoader<FileObject, Boolean>() {
		             public Boolean load(FileObject node)  {
		            	 return CachingVfsTreeModel.super.isLeaf(node);
		             }
		           });
	
	@Override
	public boolean isLeaf(FileObject node) {
		Boolean isLeaf;
		try {
			isLeaf = isLeafCache.get(node);
		} catch (ExecutionException e) {
			throw new IllegalArgumentException(e);
		}
		return isLeaf; 
	}
	
	LoadingCache<FileObject, int[]> pathCache = CacheBuilder.newBuilder()
		       .expireAfterWrite(1, TimeUnit.MINUTES)
		       .build(
		           new CacheLoader<FileObject, int[]>() {
		             public int[] load(FileObject node)  {
		            	 return CachingVfsTreeModel.super.getPath(node);
		             }
		           });

	@Override
	public int[] getPath(FileObject node) {
		int[] paths;
		try {
			paths = pathCache.get(node);
		} catch (ExecutionException e) {
			throw new IllegalArgumentException(e);
		}
		return paths;
	}
}
