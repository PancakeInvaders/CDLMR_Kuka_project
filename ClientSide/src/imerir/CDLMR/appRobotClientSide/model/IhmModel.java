package imerir.CDLMR.appRobotClientSide.model;

import java.io.File;

public class IhmModel {

	File currentlySelectedFile;

	public IhmModel() {

		currentlySelectedFile = null;

	}

	public File getCurrentlySelectedFile() {
		return currentlySelectedFile;
	}

	public void setCurrentlySelectedFile(File currentlySelectedFile) {
		this.currentlySelectedFile = currentlySelectedFile;
	}

}
