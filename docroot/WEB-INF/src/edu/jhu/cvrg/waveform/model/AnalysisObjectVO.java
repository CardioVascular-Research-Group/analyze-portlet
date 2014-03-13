package edu.jhu.cvrg.waveform.model;

import edu.jhu.cvrg.dbapi.dto.DocumentRecordDTO;

public class AnalysisObjectVO {

	private DocumentRecordDTO documentRecord;
	private FileTreeNode fileNode;
	
	public AnalysisObjectVO(FileTreeNode fileNode, DocumentRecordDTO doumentRecord) {
		this.fileNode = fileNode;
		this.documentRecord = doumentRecord;
	}
	
	public DocumentRecordDTO getDocumentRecord() {
		return documentRecord;
	}
	public void setDocumentRecord(DocumentRecordDTO doumentRecord) {
		this.documentRecord = doumentRecord;
	}
	public FileTreeNode getFileNode() {
		return fileNode;
	}
	public void setFileNode(FileTreeNode fileNode) {
		this.fileNode = fileNode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileNode == null) ? 0 : fileNode.hashCode());
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
		AnalysisObjectVO other = (AnalysisObjectVO) obj;
		if (fileNode == null) {
			if (other.fileNode != null)
				return false;
		} else if (!fileNode.equals(other.fileNode))
			return false;
		return true;
	}
	
	
	
	
}
