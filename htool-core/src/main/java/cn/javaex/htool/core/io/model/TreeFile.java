package cn.javaex.htool.core.io.model;

import java.io.File;
import java.util.List;

public class TreeFile {
	private File file;
	private List<TreeFile> children;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public List<TreeFile> getChildren() {
		return children;
	}
	public void setChildren(List<TreeFile> children) {
		this.children = children;
	}
	
}
