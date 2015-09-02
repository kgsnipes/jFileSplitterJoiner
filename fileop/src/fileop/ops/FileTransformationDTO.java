package fileop.ops;

public class FileTransformationDTO {
	
	private String originalFileName;
	private int parts;
	
	
	public FileTransformationDTO(String originalFileName, int parts) {
		super();
		this.originalFileName = originalFileName;
		this.parts = parts;
	}
	
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public int getParts() {
		return parts;
	}
	public void setParts(int parts) {
		this.parts = parts;
	}
	
	

}
